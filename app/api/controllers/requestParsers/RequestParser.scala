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

package api.controllers.requestParsers

import api.controllers.requestParsers.validators.ValidatorOld
import api.models.errors
import api.models.errors.{BadRequestError, ErrorWrapper}
import api.models.request.RawData
import utils.Logging

trait RequestParser[Raw <: RawData, Request] extends Logging {

  val validator: ValidatorOld[Raw]

  protected def requestFor(data: Raw): Request

  def parseRequest(data: Raw)(implicit correlationId: String): Either[ErrorWrapper, Request] = {
    validator.validate(data) match {
      case Nil =>
        logger.info(
          "[RequestParser][parseRequest] " +
            s"Validation successful for the request with CorrelationId: $correlationId")
        Right(requestFor(data))
      case err :: Nil =>
        logger.warn(
          "[RequestParser][parseRequest] " +
            s"Validation failed with ${err.code} error for the request with CorrelationId: $correlationId")
        Left(errors.ErrorWrapper(correlationId, err, None))
      case errs =>
        logger.warn(
          "[RequestParser][parseRequest] " +
            s"Validation failed with ${errs.map(_.code).mkString(",")} error for the request with CorrelationId: $correlationId")
        Left(errors.ErrorWrapper(correlationId, BadRequestError, Some(errs)))
    }
  }

}
