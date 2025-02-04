package com.to_do_list.dto;

import com.to_do_list.model.Priority;

public record TaskDto(String name, String description, Priority priority) {
}
