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

package v1.models.request.createAmendOtherCgt
//import api.models.domain.AssetType
import api.models.domain.AssetType
import play.api.libs.json._

case class Disposal(assetType: String,
                    assetDescription: String,
                    acquisitionDate: String,
                    disposalDate: String,
                    disposalProceeds: BigDecimal,
                    allowableCosts: BigDecimal,
                    gain: Option[BigDecimal],
                    loss: Option[BigDecimal],
                    claimOrElectionCodes: Option[Seq[String]],
                    gainAfterRelief: Option[BigDecimal],
                    lossAfterRelief: Option[BigDecimal],
                    rttTaxPaid: Option[BigDecimal])

/*
implicit val format: OFormat[Disposal] = {
  implicit val assetTypeWrites: Writes[AssetType] = implicitly[Writes[String]].contramap[AssetType](_.toDownstreamString)
  Json.format[Disposal]
}
 */

object Disposal {

  implicit val reads: Reads[Disposal] = Json.reads[Disposal]

    implicit val writes: OWrites[Disposal] = (requestBody: Disposal) => {
      val assetType = AssetType.parser(requestBody.assetType)
      Json.obj(
        "assetType" -> assetType.toDownstreamString,
        "assetDescription" -> requestBody.assetDescription,
        "acquisitionDate" -> requestBody.acquisitionDate,
        "disposalDate" -> requestBody.disposalDate,
        "disposalProceeds" -> requestBody.disposalProceeds,
        "allowableCosts" -> requestBody.allowableCosts,
        "gain" -> Writes.optionWithNull[BigDecimal].writes(requestBody.gain),
        "loss" -> Writes.optionWithNull[BigDecimal].writes(requestBody.loss),
        "claimOrElectionCodes" -> Writes.optionWithNull[Seq[String]].writes(requestBody.claimOrElectionCodes),
        "gainAfterRelief" -> Writes.optionWithNull[BigDecimal].writes(requestBody.gainAfterRelief),
        "lossAfterRelief" -> Writes.optionWithNull[BigDecimal].writes(requestBody.lossAfterRelief),
        "rttTaxPaid" -> Writes.optionWithNull[BigDecimal].writes(requestBody.rttTaxPaid)
      )
    }
  }