package com.knoldus

import com.typesafe.scalalogging.Logger
import java.util.concurrent.locks.ReentrantReadWriteLock
import scala.util.{Random, Try, Success, Failure}

class ReadWrite extends Thread {
  private val logger = Logger("ReadWrite Problem")
  private var resource: List[Int] = List.empty
  private val readWriteLock = new ReentrantReadWriteLock()

  // Method to read from the resource
  def read(): Try[List[Int]] = {
    Try {
      readWriteLock.readLock().lock()
      Thread.sleep(300)
      logger.info("Read Successful.")
      resource
    } match {
      case Success(resource) =>
        readWriteLock.readLock().unlock()
        logger.info("Reader Thread unlocked.")
        Success(resource)
      case Failure(exception) =>
        readWriteLock.readLock().unlock()
        logger.error("Error occurred while reading the resource.")
        Failure(exception)
    }
  }

  // Method to write to the resource
  def write(): Try[List[Int]] = {
    Try {
      readWriteLock.writeLock().lock()
      Thread.sleep(300)
      val element: Int = Random.nextInt()
      resource = resource :+ element
      logger.info("Write successful.")
      resource
    } match {
      case Success(resource) =>
        readWriteLock.writeLock().unlock()
        logger.info("Writer thread unlocked.")
        Success(resource)
      case Failure(exception) =>
        readWriteLock.writeLock().unlock()
        logger.error("Error occurred while writing to the resource.")
        Failure(exception)
    }
  }
}
