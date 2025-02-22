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

package common.requestParsers.validators

import shared.models.errors.MtdError
import v1.residentialPropertyDisposals.createAmendCgtPpdOverrides.def1.model.request.RawData

trait ValidatorOld[A <: RawData] {

  type ValidationLevel[T] = T => List[MtdError]

  def validate(data: A): List[MtdError]

  def run(validationSet: List[A => List[List[MtdError]]], data: A): List[MtdError] = {

    validationSet match {
      case Nil => List()
      case thisLevel :: remainingLevels =>
        thisLevel(data).flatten match {
          case x if x.nonEmpty => x
          case _               => run(remainingLevels, data)
        }
    }
  }

  def flattenErrors(errors: List[List[MtdError]]): List[MtdError] = {
    errors.flatten
      .groupBy(_.message)
      .map { case (_, errors) =>
        val baseError = errors.head.copy(paths = Some(Seq.empty[String]))

        errors.fold(baseError)((error1, error2) => {
          val paths: Option[Seq[String]] = for {
            error1Paths <- error1.paths
            error2Paths <- error2.paths
          } yield {
            error1Paths ++ error2Paths
          }
          error1.copy(paths = paths)
        })
      }
      .toList
      .sortBy(_.code)
  }

}
