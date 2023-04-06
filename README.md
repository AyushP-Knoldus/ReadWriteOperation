# ReadWriteOperation
## Problem Statement
Implement a writers-preference reader-writer lock.  
In the reader-writer problem we consider that some threads may read and some may write.  
We want to prevent more than one thread modifying the shared resource simultaneously and   
allow for two or more readers to access the shared resource at the same time.  
Make sure that if there is a writer thread waiting then it should get access to the   
resource as soon as possible i.e. as soon as it is available for someone to acquire it.  
## Code
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

