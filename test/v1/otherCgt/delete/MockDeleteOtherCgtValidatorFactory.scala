/*
 * Copyright 2024 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package v1.otherCgt.delete

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import org.scalamock.handlers.CallHandler
import org.scalamock.scalatest.MockFactory
import org.scalatest.TestSuite
import shared.controllers.validators.Validator
import shared.models.errors.MtdError
import v1.otherCgt.delete.model.request.DeleteOtherCgtRequestData

trait MockDeleteOtherCgtValidatorFactory extends MockFactory with TestSuite {

  val mockDeleteOtherCgtValidatorFactory: DeleteOtherCgtValidatorFactory =
    mock[DeleteOtherCgtValidatorFactory]

  object MockedDeleteOtherCgtValidatorFactory {

    def validator(): CallHandler[Validator[DeleteOtherCgtRequestData]] =
      (mockDeleteOtherCgtValidatorFactory.validator(_: String, _: String)).expects(*, *)

  }

  def willUseValidator(use: Validator[DeleteOtherCgtRequestData]): CallHandler[Validator[DeleteOtherCgtRequestData]] = {
    MockedDeleteOtherCgtValidatorFactory
      .validator()
      .anyNumberOfTimes()
      .returns(use)
  }

  def returningSuccess(result: DeleteOtherCgtRequestData): Validator[DeleteOtherCgtRequestData] =
    new Validator[DeleteOtherCgtRequestData] {
      def validate: Validated[Seq[MtdError], DeleteOtherCgtRequestData] = Valid(result)
    }

  def returning(result: MtdError*): Validator[DeleteOtherCgtRequestData] = returningErrors(result)

  def returningErrors(result: Seq[MtdError]): Validator[DeleteOtherCgtRequestData] =
    new Validator[DeleteOtherCgtRequestData] {
      def validate: Validated[Seq[MtdError], DeleteOtherCgtRequestData] = Invalid(result)
    }

}
