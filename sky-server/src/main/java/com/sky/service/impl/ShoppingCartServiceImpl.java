package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        // 只能设置自己的购物车
        shoppingCart.setUserId(BaseContext.getCurrentId());

        // 查询是否已存在该商品
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);

        if (shoppingCartList != null && !shoppingCartList.isEmpty()) {
            // 已经存在，数量增加 1
            shoppingCart = shoppingCartList.getFirst();
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(shoppingCart);
        } else {
            // 不存在，新增一条记录
            // 检查是套餐还是菜品
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                // 菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                // 套餐
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查看购物车
     *
     * @return
     */
    @Override
    public List<ShoppingCart> showShoppingCart() {
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(BaseContext.getCurrentId()).build();
        return shoppingCartMapper.list(shoppingCart);
    }

    /**
     * 清空购物车
     */
    @Override
    public void cleanShoppingCart() {
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
    }

    /**
     * 删除购物车中一个商品
     *
     * @param shoppingCartDTO
     */
    @Override
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList != null && !shoppingCartList.isEmpty()) {
            shoppingCart = shoppingCartList.getFirst();

            Integer number = shoppingCart.getNumber();
            if (number == 1) {
                // 商品数量等于1，直接删除该商品
                shoppingCartMapper.deleteById(shoppingCart.getId());
            } else {
                // 商品数量大于1，商品数量-1
                shoppingCart.setNumber(number - 1);
                shoppingCartMapper.updateNumberById(shoppingCart);
            }
        }

    }
}
