package com.noblesoftware.portalcore.component.compose

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Shape
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalShapes

data class DefaultButtonVariant(
    val backgroundColor: Int,
    val contentColor: Int,
    val borderColor: Int,
)

@JvmInline
value class ButtonVariant internal constructor(@Suppress("unused") private val value: Int) {

    override fun toString(): String {
        return when (this) {
            Primary -> "Primary"
            Neutral -> "Neutral"
            Danger -> "Danger"
            Success -> "Success"
            Warning -> "Warning"
            else -> "Invalid"
        }
    }

    companion object {
        @Stable
        val Primary: ButtonVariant = ButtonVariant(0)

        @Stable
        val Neutral: ButtonVariant = ButtonVariant(1)

        @Stable
        val Danger: ButtonVariant = ButtonVariant(2)

        @Stable
        val Success: ButtonVariant = ButtonVariant(3)

        @Stable
        val Warning: ButtonVariant = ButtonVariant(4)
    }
}

@JvmInline
value class ButtonSize internal constructor(@Suppress("unused") private val value: Int) {

    override fun toString(): String {
        return when (this) {
            Medium -> "Medium"
            Small -> "Small"
            Large -> "Large"
            else -> "Invalid"
        }
    }

    companion object {
        @Stable
        val Medium: ButtonSize = ButtonSize(0)

        @Stable
        val Small: ButtonSize = ButtonSize(1)

        @Stable
        val Large: ButtonSize = ButtonSize(2)
    }
}

@JvmInline
value class ButtonType internal constructor(@Suppress("unused") private val value: Int) {


    override fun toString(): String {
        return when (this) {
            Solid -> "Solid"
            Outlined -> "Outlined"
            Plain -> "Plain"
            Soft -> "Soft"
            else -> "Invalid"
        }
    }

    companion object {
        @Stable
        val Solid: ButtonType = ButtonType(0)

        @Stable
        val Outlined: ButtonType = ButtonType(1)

        @Stable
        val Plain: ButtonType = ButtonType(2)

        @Stable
        val Soft: ButtonType = ButtonType(3)
    }
}

fun ButtonVariant.getColor(buttonType: ButtonType): DefaultButtonVariant {
    when (this) {
        // variant -> background, foreground (such as button color, text color, border color)
        ButtonVariant.Primary -> return when (buttonType) {
            ButtonType.Solid -> DefaultButtonVariant(
                R.color.primary_solid_bg,
                R.color.primary_solid_color,
                R.color.primary_solid_bg
            )

            ButtonType.Outlined -> DefaultButtonVariant(
                R.color.primary_solid_color,
                R.color.primary_solid_bg,
                R.color.primary_solid_bg
            )
            ButtonType.Plain -> DefaultButtonVariant(
                R.color.primary_solid_color,
                R.color.primary_solid_bg,
                R.color.primary_solid_color
            )

            ButtonType.Soft -> DefaultButtonVariant(
                R.color.primary_soft_bg,
                R.color.primary_soft_color,
                R.color.primary_soft_bg
            )
            else -> DefaultButtonVariant(
                R.color.primary_solid_bg,
                R.color.primary_solid_color,
                R.color.primary_solid_bg
            )
        }

        ButtonVariant.Neutral -> return when (buttonType) {
            ButtonType.Solid -> DefaultButtonVariant(
                R.color.neutral_solid_bg,
                R.color.neutral_solid_color,
                R.color.neutral_solid_bg
            )

            ButtonType.Outlined -> DefaultButtonVariant(
                R.color.neutral_solid_color,
                R.color.neutral_solid_bg,
                R.color.neutral_outlined_border
            )

            ButtonType.Plain -> DefaultButtonVariant(
                R.color.neutral_solid_color,
                R.color.neutral_solid_bg,
                R.color.neutral_solid_color
            )

            ButtonType.Soft -> DefaultButtonVariant(
                R.color.neutral_soft_bg,
                R.color.neutral_soft_color,
                R.color.neutral_soft_bg
            )

            else -> DefaultButtonVariant(
                R.color.neutral_solid_bg,
                R.color.neutral_solid_color,
                R.color.neutral_solid_bg
            )
        }

        ButtonVariant.Danger -> return when (buttonType) {
            ButtonType.Solid -> DefaultButtonVariant(
                R.color.danger_solid_bg,
                R.color.danger_solid_color,
                R.color.danger_solid_bg
            )

            ButtonType.Outlined -> DefaultButtonVariant(
                R.color.danger_solid_color,
                R.color.danger_solid_bg,
                R.color.danger_solid_bg
            )

            ButtonType.Plain -> DefaultButtonVariant(
                R.color.danger_solid_color,
                R.color.danger_solid_bg,
                R.color.danger_solid_color
            )

            ButtonType.Soft -> DefaultButtonVariant(
                R.color.danger_soft_bg,
                R.color.danger_soft_color,
                R.color.danger_soft_bg
            )

            else -> DefaultButtonVariant(
                R.color.danger_solid_bg,
                R.color.danger_solid_color,
                R.color.danger_solid_bg
            )
        }

        ButtonVariant.Success -> return when (buttonType) {
            ButtonType.Solid -> DefaultButtonVariant(
                R.color.success_solid_bg,
                R.color.success_solid_color,
                R.color.success_solid_bg
            )

            ButtonType.Outlined -> DefaultButtonVariant(
                R.color.success_solid_color,
                R.color.success_solid_bg,
                R.color.success_solid_bg
            )

            ButtonType.Plain -> DefaultButtonVariant(
                R.color.success_solid_color,
                R.color.success_solid_bg,
                R.color.success_solid_color
            )

            ButtonType.Soft -> DefaultButtonVariant(
                R.color.success_soft_bg,
                R.color.success_soft_color,
                R.color.success_soft_bg
            )

            else -> DefaultButtonVariant(
                R.color.success_solid_bg,
                R.color.success_solid_color,
                R.color.success_solid_bg
            )
        }

        ButtonVariant.Warning -> return when (buttonType) {
            ButtonType.Solid -> DefaultButtonVariant(
                R.color.warning_solid_bg,
                R.color.warning_solid_color,
                R.color.warning_solid_bg
            )

            ButtonType.Outlined -> DefaultButtonVariant(
                R.color.warning_solid_color,
                R.color.warning_solid_bg,
                R.color.warning_solid_bg
            )

            ButtonType.Plain -> DefaultButtonVariant(
                R.color.warning_solid_color,
                R.color.warning_solid_bg,
                R.color.warning_solid_color
            )

            ButtonType.Soft -> DefaultButtonVariant(
                R.color.warning_soft_bg,
                R.color.warning_soft_color,
                R.color.warning_soft_bg
            )

            else -> DefaultButtonVariant(
                R.color.warning_solid_bg,
                R.color.warning_solid_color,
                R.color.warning_solid_bg
            )
        }

        else -> return when (buttonType) {
            ButtonType.Solid -> DefaultButtonVariant(
                R.color.primary_solid_bg,
                R.color.primary_solid_color,
                R.color.primary_solid_bg
            )

            ButtonType.Outlined -> DefaultButtonVariant(
                R.color.primary_solid_color,
                R.color.primary_solid_bg,
                R.color.primary_solid_bg
            )
            ButtonType.Plain -> DefaultButtonVariant(
                R.color.primary_solid_color,
                R.color.primary_solid_bg,
                R.color.primary_solid_color
            )

            ButtonType.Soft -> DefaultButtonVariant(
                R.color.primary_soft_bg,
                R.color.primary_soft_color,
                R.color.primary_soft_bg
            )
            else -> DefaultButtonVariant(
                R.color.primary_solid_bg,
                R.color.primary_solid_color,
                R.color.primary_solid_bg
            )
        }
    }
}

fun ButtonSize.getButtonHeight(): Int {
    return when (this) {
        ButtonSize.Medium -> R.dimen.button_medium_height
        ButtonSize.Small -> R.dimen.button_small_height
        ButtonSize.Large -> R.dimen.button_large_height
        else -> R.dimen.button_medium_height
    }
}

fun ButtonSize.getButtonRadius(): Shape {
    return when (this) {
        ButtonSize.Medium -> LocalShapes.small
        ButtonSize.Small -> LocalShapes.small
        ButtonSize.Large -> LocalShapes.medium
        else -> LocalShapes.medium
    }
}