/*
 * Copyright 2023 HM Revenue & Customs
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

package api.controllers.validators.resolvers

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import api.models.errors.{MtdError, RuleTaxYearRangeInvalidError}

import java.time.LocalDate
import java.time.format.DateTimeParseException

/** Checks that the date format is YYYY-MM-DD, and returns a new LocalDate.
  */
case class ResolveIsoDate(error: MtdError) extends ResolverSupport {

  val resolver: Resolver[String, LocalDate] = value =>
    try Valid(LocalDate.parse(value))
    catch {
      case _: DateTimeParseException => Invalid(List(error))
    }

  def apply(value: String): Validated[Seq[MtdError], LocalDate] = resolver(value)

  def apply(maybeValue: Option[String]): Validated[Seq[MtdError], Option[LocalDate]] =
    resolver.resolveOptionally(maybeValue)

}

object ResolveIsoDate extends ResolverSupport {

  private def isDateRangeValid(date: LocalDate): Boolean            = date.getYear >= 1900 && date.getYear <= 2100

  def apply(value: String, error: MtdError): Validated[Seq[MtdError], LocalDate] =
    ResolveIsoDate(error).resolver(value)

  def apply(value: Option[String], error: MtdError): Validated[Seq[MtdError], Option[LocalDate]] = {
    val resolver = ResolveIsoDate(error).resolver.resolveOptionally
    resolver(value)
  }
  def apply(value: String, error: MtdError, isDateRangeCheck: Boolean): Validated[Seq[MtdError], Option[LocalDate]] = {
    val resolver = ResolveIsoDate(error).resolver
    resolver(value).andThen { dateOption =>
      if (isDateRangeCheck) {
        dateOption match {
          case (date) if isDateRangeValid(date) => Valid(dateOption)
          case _ => Invalid(List(RuleTaxYearRangeInvalidError))
        }
      } else {
        Valid(())
      }
    }
  }


}
