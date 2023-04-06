package com.knoldus

import com.typesafe.scalalogging.Logger
import scala.util.{Failure, Success, Try}

object Driver extends App {

  // Create a logger instance
  private val logger = Logger("ReadWrite Problem")

  // Create an instance of the ReadWrite class
  private val readWrite = new ReadWrite

  // Create threads for reading and writing to the resource
  private val readThread1 = new Thread(() => {
    val result=handleException(readWrite.read())
    logger.info(s"Read ->$result")
  })
  private val readThread2 = new Thread(() => {
    val result = handleException(readWrite.read())
    logger.info(s"Read ->$result")
  })
  private val writeThread1 = new Thread(() => {
    val result = handleException(readWrite.write())
    logger.info(s"Write ->$result")
  })
  private val writeThread2 = new Thread(() => {
    val result = handleException(readWrite.write())
    logger.info(s"Write ->$result")
  })

  // Start the threads
  readThread1.start()
  readThread2.start()
  writeThread1.start()
  writeThread2.start()

  // Wait for all threads to finish
  readThread1.join()
  readThread2.join()
  writeThread1.join()
  writeThread2.join()

  // A utility method to handle any exceptions and return the result if successful
  private def handleException(result : Try[List[Int]]): List[Int]={
    result match {
      case Failure(exception) =>logger.error(exception.getMessage)
        List()
      case Success(value) => value
    }
  }
}
