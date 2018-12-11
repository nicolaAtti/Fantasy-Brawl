package communication

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import com.spingo.op_rabbit.{RabbitMarshaller, RabbitUnmarshaller}

/** Provides features of Marshaller and Unmarshaller to messages through the use of serialization. */
object MessageFormat {

  type MyFormat[A] = RabbitMarshaller[A] with RabbitUnmarshaller[A]
  import Serializer._

  /** Provides Marshaller and Unmarshaller to messages. */
  def format[A]: MyFormat[A] = new RabbitMarshaller[A] with RabbitUnmarshaller[A] {
    val contentType = "text/plain"
    protected val contentEncoding = Some("UTF-8")
    def marshall(value: A): Array[Byte] = serialize(value)

    def unmarshall(value: Array[Byte], contentType: Option[String], charset: Option[String]): A = deserialize(value)
  }

  private object Serializer {

    def serialize[A](value: A): Array[Byte] = {
      val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
      val oos = new ObjectOutputStream(stream)
      oos.writeObject(value)
      oos.close()
      stream.toByteArray
    }

    def deserialize[A](bytes: Array[Byte]): A = {
      val ois = new ObjectInputStream(new ByteArrayInputStream(bytes))
      val value = ois.readObject
      ois.close()
      value.asInstanceOf[A]
    }
  }
}
