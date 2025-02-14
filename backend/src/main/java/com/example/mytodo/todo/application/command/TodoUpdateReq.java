package com.example.mytodo.todo.application.command;

public record TodoUpdateReq(String type, String content, Boolean complete) {}
