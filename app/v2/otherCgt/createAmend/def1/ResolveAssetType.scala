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

package v2.otherCgt.createAmend.def1

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import shared.controllers.validators.resolvers.ResolverSupport
import shared.models.errors.MtdError

case class ResolveAssetType(error: MtdError) extends ResolverSupport {

  val resolver: Resolver[String, String] = value =>
    if (isValid(value))
      Valid(value)
    else
      Invalid(List(error))

  def apply(value: String): Validated[Seq[MtdError], String] = resolver(value)

  def isValid(assetType: String): Boolean = assetType match {
    case "other-property"  => true
    case "unlisted-shares" => true
    case "listed-shares"   => true
    case "other-asset"     => true
    case _                 => false
  }

}

object ResolveAssetType {

  def apply(value: String, error: MtdError): Validated[Seq[MtdError], String] = {
    val resolver = ResolveAssetType(error)
    resolver(value)
  }

}
