package ru.otus.otuskotlin.tasktracker.mappers.v1.exceptions

class UnknownRequestClass(clazz: Class<*>) : RuntimeException("Class $clazz cannot be mapped to MkplContext")
