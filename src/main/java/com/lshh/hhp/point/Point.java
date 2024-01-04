package com.lshh.hhp.point;

import com.lshh.hhp.orderItem.OrderItem;
import com.lshh.hhp.payment.Payment;
import com.lshh.hhp.point.dto.PointDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Getter
@Accessors(fluent = true)
@Table(name = "tb_point")
public class Point {
    public enum PointType{
        SUM(0),
        PAYMENT(1),
        PURCHASE(2);

        PointType(int code) {}
        public static PointType of(final int code){
            return PointType.values()[code];
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Integer count;

    Long userId;

    Integer fromType;   // PointService.PointType
    Long fromId;    // PaymentId or PurchaseId

    public static Point createNewAddPoint(Payment payment){
        Point newOne = new Point();
        newOne.count = payment.into();
        newOne.userId = payment.userId();
        newOne.fromType = PointType.PAYMENT.ordinal();
        newOne.fromId = payment.id();
        return newOne;
    }
    public static Point createNewSubtractPoint(OrderItem orderItem){
        Point newOne = new Point();
        newOne.count = -1 * orderItem.paid();
        newOne.userId = orderItem.userId();
        newOne.fromType = PointType.PURCHASE.ordinal();
        newOne.fromId = orderItem.id();
        return newOne;
    }
    public static Point createNewSquashedPoint(VPoint vPoint){
        Point newOne = new Point();
        newOne.userId = vPoint.userId();
        newOne.fromType = PointType.SUM.ordinal();
        newOne.count = vPoint.remain();
        return newOne;
    }

    public static List<Point> createNewSubtractPoints(List<OrderItem> orderItems){
        return orderItems.stream().map(Point::createNewSubtractPoint).toList();
    }

    public Point setDisable(){
        this.count = 0;
        return this;
    }
    public static List<Point> setDisable(List<Point> points){
        points.forEach(Point::setDisable);
        return points;
    }

    public PointDto toDto(){
        return new PointDto()
            .id(this.id)
            .userId(this.userId)
            .fromType(this.fromType)
            .fromId(this.fromId)
            .count(this.count);
    }
    static public Point toEntity(PointDto dto){
        Point newOne = new Point();
        newOne.id = dto.id();
        newOne.userId = dto.userId();
        newOne.fromType = dto.fromType();
        newOne.fromId = dto.fromId();
        newOne.count = dto.count();
        return newOne;
    }
}
