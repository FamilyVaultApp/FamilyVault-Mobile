package com.github.familyvault.forms.validator

import com.github.familyvault.models.FamilyMember

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

        fun validateTooLong(
            value: String,
            maxLength: Int = FormValidatorConfig.DEFAULT_MAX_LENGTH
        ): FormValidatorError? {
            if (value.length > maxLength) {
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

        fun validateGroupNotEmpty(chatMembers: List<FamilyMember>): FormValidatorError? {
            if (chatMembers.isEmpty()) {
                return FormValidatorError.CHAT_GROUP_MEMBERS_EMPTY
            }
            return null
        }

        fun validateCreatorIsGroupMember(
            chatMembers: List<FamilyMember>,
            currentUserPubKey: String
        ): FormValidatorError? {
            if (!chatMembers.any { it.publicKey == currentUserPubKey }) {
                return FormValidatorError.CREATOR_NOT_IN_GROUP
            }
            return null
        }
    }
}