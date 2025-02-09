package com.example.foodordersystem.Repository;

import com.example.foodordersystem.Entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

public class OrderRepositoryImpl implements CustomOrderRepository{
    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Order> findAllOrders(){
        QOrder order = QOrder.order;
        QUser user = QUser.user;
        QOrderItem orderItem = QOrderItem.orderItem;
        QFood food = QFood.food;

        return queryFactory
                .selectFrom(order)
                .leftJoin(order.user, user).fetchJoin()
                .leftJoin(order.orderItems, orderItem).fetchJoin()
                .leftJoin(orderItem.food, food).fetchJoin()
                .fetch();
    }

    public List<Food> findFoodById(List<Long> foodId) {
        QFood food = QFood.food;

        return queryFactory
                .selectFrom(food)
                .where(food.id.in(foodId))
                .fetch();
    }

    @Override
    @Transactional
    public void deleteOrderById(Long orderId) {
        QOrderItem orderItem = QOrderItem.orderItem;
        QOrder order = QOrder.order;

        queryFactory.delete(orderItem)
                .where(orderItem.order.id.eq(orderId))
                .execute();

        queryFactory.delete(order)
                .where(order.id.eq(orderId))
                .execute();
    }


}
