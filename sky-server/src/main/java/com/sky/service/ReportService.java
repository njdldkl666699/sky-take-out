package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;

public interface ReportService {

    /**
     * 统计[begin, end]日期内每天的营业额
     *
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计[begin, end]日期内每天的用户数量
     *
     * @param begin
     * @param end
     * @return
     */
    UserReportVO userStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计[begin, end]日期内每天的订单信息
     *
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO ordersStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计[begin, end]日期内销量前十的商品（包括菜品和套餐）
     *
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO salesTop10(LocalDate begin, LocalDate end);

    /**
     * 导出近30天的运营数据报表
     *
     * @param response
     */
    void exportBusinessData(HttpServletResponse response);
}
