package com.surest.dto.mapper;

import com.surest.dto.MemberDto;
import com.surest.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
  Member toEntity(MemberDto dto);

  MemberDto toDto(Member entity);
}
