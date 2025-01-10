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

package v1.otherCgt.retrieve.def1.model.response

import play.api.libs.json.Reads
import shared.utils.enums.Enums

sealed trait DownstreamAssetType {
  def toMtd: AssetType
}

object DownstreamAssetType {

  case object `otherProperty` extends DownstreamAssetType {
    override def toMtd: AssetType = AssetType.`other-property`
  }

  case object `unlistedShares` extends DownstreamAssetType {
    override def toMtd: AssetType = AssetType.`unlisted-shares`
  }

  case object `listedShares` extends DownstreamAssetType {
    override def toMtd: AssetType = AssetType.`listed-shares`
  }

  case object `otherAsset` extends DownstreamAssetType {
    override def toMtd: AssetType = AssetType.`other-asset`
  }

  implicit val reads: Reads[DownstreamAssetType] = Enums.reads[DownstreamAssetType]
}
