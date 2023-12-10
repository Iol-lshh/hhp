package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.dto.PointDto;
import com.lshh.hhp.orm.entity.Point;
import com.lshh.hhp.orm.repository.PointRepository;
import com.lshh.hhp.orm.repository.VPointRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PointServiceDefault implements PointService {

    final PointRepository pointRepository;
    final VPointRepository vPointRepository;

    public PointDto toDto(Point entity){
        return new PointDto()
                .id(entity.id())
                .userId(entity.userId())
                .fromType(entity.fromType())
                .fromId(entity.fromId())
                .count(entity.count());
    }
    public Point toEntity(PointDto dto){
        return new Point()
                .id(dto.id())
                .userId(dto.userId())
                .fromType(dto.fromType())
                .fromId(dto.fromId())
                .count(dto.count());
    }
    @Override
    public Result save(PointDto dto) throws Exception {
        pointRepository.save(toEntity(dto));
        return Result.OK;
    }

    @Override
    public List<PointDto> findAll() {
        return pointRepository
                .findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public Optional<PointDto> find(long id) {
        return pointRepository
                .findById(id)
                .map(this::toDto);
    }

    @Override
    public List<PointDto> findAllByUserId(long userId) {
        return pointRepository
                .findAllByUserId(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public Integer remain(long userId) {
        return vPointRepository
                .findById(userId)
                .map(e->e.remain())
                .orElse(0);
    }
}
