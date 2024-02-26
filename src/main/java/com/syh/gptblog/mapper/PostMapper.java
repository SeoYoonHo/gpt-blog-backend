package com.syh.gptblog.mapper;

import com.syh.gptblog.domain.Category;
import com.syh.gptblog.domain.Post;
import com.syh.gptblog.domain.PostCategory;
import com.syh.gptblog.dto.PostDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    PostDto.GptAnswerResponse postToGptResponseDto(Post post);

    @Mapping(target = "categories", ignore = true)
    Post gptResponseDtoToPost(PostDto.GptAnswerResponse gptAnswerResponse);

    @Mapping(target = "categories", source = "categories")
    PostDto.PostResponse postToPostResponseDto(Post post);

    default List<String> categoriesToCategoryNameList(List<PostCategory> categories) {
        if (categories == null) {
            return null;
        }
        return categories.stream()
                         .map(PostCategory::getCategory)
                         .map(Category::getName)
                         .collect(Collectors.toList());
    }
}
