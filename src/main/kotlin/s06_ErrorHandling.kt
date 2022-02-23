import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.NoSuchBucket

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.LimitExceededException
import aws.smithy.kotlin.runtime.ErrorMetadata
import aws.smithy.kotlin.runtime.http.engine.HttpClientEngine
import aws.smithy.kotlin.runtime.http.engine.ktor.KtorEngine
import aws.smithy.kotlin.runtime.http.request.HttpRequest
import aws.smithy.kotlin.runtime.http.response.HttpCall
import aws.smithy.kotlin.runtime.util.InternalApi

/**
 * For demoing retries only. Don't use something like this in production!
 */
class FaultyHttpClient : HttpClientEngine {
    private val underlyingClient = KtorEngine()
    private var requestNumber = 0

    override val coroutineContext = underlyingClient.coroutineContext

    @OptIn(InternalApi::class)
    override suspend fun roundTrip(request: HttpRequest): HttpCall {
        if (++requestNumber % 2 == 0) {
            return underlyingClient.roundTrip(request)
        } else {
            println("Oops, this request failed!")
            throw LimitExceededException { message = "Too fast!" }.also {
                it.sdkErrorMetadata.attributes[ErrorMetadata.Retryable] = true
            }
        }
    }
}


suspend fun main() {
    val s3 = S3Client.fromEnvironment {
        httpClientEngine = FaultyHttpClient()
    }
    s3.listObjectsV2 {
        bucket = "ktwb-demo"
    }.contents?.forEach { println(it.key) }
}
