package com.syh.gptblog.mapper;

import com.syh.gptblog.domain.Post;
import com.syh.gptblog.dto.PostDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    PostDto.GptAnswerResponse postToGptResponseDto(Post post);
}
