package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.exception.BaseException;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 统计[begin, end]日期内每天的营业额
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        // 构造日期列表
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate temp = begin;
        dateList.add(temp);

        while (temp.isBefore(end)) {
            temp = temp.plusDays(1);
            dateList.add(temp);
        }

        // 构造营业额列表
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = date.atStartOfDay();
            LocalDateTime endTime = date.atTime(LocalTime.MAX);

            Map<String, Object> map = new HashMap<>();
            map.put("status", Orders.COMPLETED);
            map.put("begin", beginTime);
            map.put("end", endTime);
            Double turnover = orderMapper.sumTurnoverByMap(map);

            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }

        // 构造VO并返回
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ','))
                .turnoverList(StringUtils.join(turnoverList, ','))
                .build();
    }

    /**
     * 统计[begin, end]日期内每天的用户数量
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        // 构造日期列表
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate temp = begin;
        dateList.add(temp);

        while (temp.isBefore(end)) {
            temp = temp.plusDays(1);
            dateList.add(temp);
        }

        // 总用户数列表
        List<Integer> totalUserList = new ArrayList<>();
        // 新增用户数列表
        List<Integer> newUserList = new ArrayList<>();

        // begin前一天的23:59:59.999999999之前所有用户数量
        LocalDateTime originalTime = begin.minusDays(1).atTime(LocalTime.MAX);
        Integer totalUser = userMapper.countByTimeRange(null, originalTime);
        totalUser = totalUser == null ? 0 : totalUser;

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = date.atStartOfDay();
            LocalDateTime endTime = date.atTime(LocalTime.MAX);

            // 每次遍历查询当天新增用户数
            Integer newUser = userMapper.countByTimeRange(beginTime, endTime);
            newUser = newUser == null ? 0 : newUser;
            newUserList.add(newUser);

            // 加上前一天的总和即为当前一天的总用户数
            totalUser += newUser;
            totalUserList.add(totalUser);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ','))
                .newUserList(StringUtils.join(newUserList, ','))
                .totalUserList(StringUtils.join(totalUserList, ','))
                .build();
    }

    /**
     * 统计[begin, end]日期内每天的订单信息
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        // 构造日期列表
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate temp = begin;
        dateList.add(temp);

        while (temp.isBefore(end)) {
            temp = temp.plusDays(1);
            dateList.add(temp);
        }

        // 订单数列表
        List<Integer> orderCountList = new ArrayList<>();
        // 有效订单数列表
        List<Integer> validOrderCountList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = date.atStartOfDay();
            LocalDateTime endTime = date.atTime(LocalTime.MAX);

            // 统计当天订单数
            int orderCount = getOrderCount(null, beginTime, endTime);
            orderCountList.add(orderCount);

            // 统计当天有效订单数
            int validOrderCount = getOrderCount(Orders.COMPLETED, beginTime, endTime);
            validOrderCountList.add(validOrderCount);
        }

        // 订单总数
        int totalOrderCount = orderCountList.stream().mapToInt(Integer::intValue).sum();
        // 有效订单数
        int validOrderCount = validOrderCountList.stream().mapToInt(Integer::intValue).sum();
        // 订单完成率
        double orderCompletionRate = 0.0;
        if (totalOrderCount != 0) {
            orderCompletionRate = (double) validOrderCount / totalOrderCount;
        }

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ','))
                .orderCountList(StringUtils.join(orderCountList, ','))
                .validOrderCountList(StringUtils.join(validOrderCountList, ','))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 统计[begin, end]日期内销量前十的商品（包括菜品和套餐）
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO salesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = begin.atStartOfDay();
        LocalDateTime endTime = end.atTime(LocalTime.MAX);

        List<GoodsSalesDTO> goodsSalesDTOList =
                orderDetailMapper.getSalesTop10ByTimeRange(beginTime, endTime);

        List<String> nameList = goodsSalesDTOList.stream()
                .map(GoodsSalesDTO::getName).toList();
        List<Integer> numberList = goodsSalesDTOList.stream()
                .map(GoodsSalesDTO::getNumber).toList();

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList, ','))
                .numberList(StringUtils.join(numberList, ','))
                .build();
    }

    /**
     * 导出近30天的运营数据报表
     *
     * @param response
     */
    @Override
    public void exportBusinessData(HttpServletResponse response) {
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);

        // 查询概览运营数据，提供给Excel模板文件
        BusinessDataVO businessData = workspaceService
                .businessData(begin.atStartOfDay(), end.atTime(LocalTime.MAX));
        InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream("template/运营数据报表模板.xlsx");
        if (inputStream == null) {
            throw new BaseException(MessageConstant.UNKNOWN_ERROR);
        }

        try (inputStream) {
            // 基于提供好的模板文件创建一个新的Excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);
            // 获取Excel文件中的一个Sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            // B2：日期范围
            sheet.getRow(1).getCell(1).setCellValue("时间：" + begin + "至" + end);
            // 第4行
            XSSFRow row4 = sheet.getRow(3);
            row4.getCell(2).setCellValue(businessData.getTurnover());
            row4.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row4.getCell(6).setCellValue(businessData.getNewUsers());
            // 第5行
            XSSFRow row5 = sheet.getRow(4);
            row5.getCell(2).setCellValue(businessData.getValidOrderCount());
            row5.getCell(4).setCellValue(businessData.getUnitPrice());

            // 30天
            for (int i = 0; i < 30; i++) {
                LocalDate date = begin.plusDays(i);
                // 准备明细数据
                BusinessDataVO oneDayBusinessData = workspaceService.businessData(date.atStartOfDay(), date.atTime(LocalTime.MAX));

                XSSFRow row = sheet.getRow(i + 7);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(oneDayBusinessData.getTurnover());
                row.getCell(3).setCellValue(oneDayBusinessData.getValidOrderCount());
                row.getCell(4).setCellValue(oneDayBusinessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(oneDayBusinessData.getUnitPrice());
                row.getCell(6).setCellValue(oneDayBusinessData.getNewUsers());
            }

            // 通过输出流将文件下载到客户端浏览器中
            ServletOutputStream outputStream = response.getOutputStream();
            excel.write(outputStream);
            // 关闭资源
            outputStream.flush();
            outputStream.close();
            excel.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 根据时间区间统计指定状态的订单数量
     *
     * @param status
     * @param beginTime
     * @param endTime
     * @return
     */
    private int getOrderCount(Integer status, LocalDateTime beginTime, LocalDateTime endTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("begin", beginTime);
        map.put("end", endTime);
        Integer count = orderMapper.countByMap(map);
        return count == null ? 0 : count;
    }
}
