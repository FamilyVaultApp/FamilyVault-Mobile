package com.github.familyvault.utils.form

class FormValidator {
    companion object {
        fun multipleValidators(vararg validatorResult: FormValidatorError?): FormValidatorError? {
            return validatorResult.firstOrNull { it != null }
        }

        fun validateTooShort(value: String): FormValidatorError? {
            if (value.length < FormValidatorConfig.MIN_LENGTH) {
                return FormValidatorError.TOO_SHORT
            }
            return null
        }

        fun validateTooLong(value: String): FormValidatorError? {
            if (value.length > FormValidatorConfig.MAX_LENGTH) {
                return FormValidatorError.TOO_LONG
            }
            return null
        }

        fun validateEmpty(value: String): FormValidatorError? {
            if (value.isEmpty()) {
                return FormValidatorError.IS_EMPTY
            }
            return null
        }

        fun validateNotEqual(firstValue: String, secondValue: String): FormValidatorError? {
            if (firstValue != secondValue) {
                return FormValidatorError.NOT_EQUAL
            }
            return null
        }
    }
}