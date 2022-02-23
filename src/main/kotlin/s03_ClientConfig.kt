import aws.sdk.kotlin.runtime.auth.credentials.ProfileCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.smithy.kotlin.runtime.http.engine.ktor.KtorEngine

suspend fun main() {

    // Detect everything!
    val s3 = S3Client.fromEnvironment { }

    // Detect most things, override something
    val s3_2 = S3Client.fromEnvironment {
        region = "us-east-1"
        credentialsProvider = ProfileCredentialsProvider()
    }

    // Don't detect (no suspend call)
    val s3_3 = S3Client {
        region = "us-east-1"
        credentialsProvider = ProfileCredentialsProvider()
        httpClientEngine = KtorEngine()
    }
}
