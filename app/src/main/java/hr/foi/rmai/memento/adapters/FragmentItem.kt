package hr.foi.rmai.memento.adapters

import kotlin.reflect.KClass

data class FragmentItem(val titleRes: Int, val iconRes: Int, val fragmentClass: KClass<*>)