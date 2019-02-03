package com.fjrc.todo.util.bottomsheet

data class BottomMenuItem(
        val resId: Int,
        val name: String,
        val action: () -> Unit
)