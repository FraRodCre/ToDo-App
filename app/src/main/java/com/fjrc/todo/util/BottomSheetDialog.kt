package com.fjrc.todo.util

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.fjrc.todo.R

abstract class BottomSheetDialog : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.Dialog

}