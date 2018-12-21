Index.PACKAGES = {"org" : [], "org.mongodb" : [], "org.mongodb.scala" : [], "turnordering" : [{"name" : "turnordering.AsyncDbManager", "members_trait" : [{"label" : "synchronized", "tail" : "(arg0: ⇒ T0): T0", "member" : "scala.AnyRef.synchronized", "link" : "turnordering\/AsyncDbManager.html#synchronized[T0](x$1:=>T0):T0", "kind" : "final def"}, {"label" : "##", "tail" : "(): Int", "member" : "scala.AnyRef.##", "link" : "turnordering\/AsyncDbManager.html###():Int", "kind" : "final def"}, {"label" : "!=", "tail" : "(arg0: Any): Boolean", "member" : "scala.AnyRef.!=", "link" : "turnordering\/AsyncDbManager.html#!=(x$1:Any):Boolean", "kind" : "final def"}, {"label" : "==", "tail" : "(arg0: Any): Boolean", "member" : "scala.AnyRef.==", "link" : "turnordering\/AsyncDbManager.html#==(x$1:Any):Boolean", "kind" : "final def"}, {"label" : "ne", "tail" : "(arg0: AnyRef): Boolean", "member" : "scala.AnyRef.ne", "link" : "turnordering\/AsyncDbManager.html#ne(x$1:AnyRef):Boolean", "kind" : "final def"}, {"label" : "eq", "tail" : "(arg0: AnyRef): Boolean", "member" : "scala.AnyRef.eq", "link" : "turnordering\/AsyncDbManager.html#eq(x$1:AnyRef):Boolean", "kind" : "final def"}, {"label" : "finalize", "tail" : "(): Unit", "member" : "scala.AnyRef.finalize", "link" : "turnordering\/AsyncDbManager.html#finalize():Unit", "kind" : "def"}, {"label" : "wait", "tail" : "(): Unit", "member" : "scala.AnyRef.wait", "link" : "turnordering\/AsyncDbManager.html#wait():Unit", "kind" : "final def"}, {"label" : "wait", "tail" : "(arg0: Long, arg1: Int): Unit", "member" : "scala.AnyRef.wait", "link" : "turnordering\/AsyncDbManager.html#wait(x$1:Long,x$2:Int):Unit", "kind" : "final def"}, {"label" : "wait", "tail" : "(arg0: Long): Unit", "member" : "scala.AnyRef.wait", "link" : "turnordering\/AsyncDbManager.html#wait(x$1:Long):Unit", "kind" : "final def"}, {"label" : "notifyAll", "tail" : "(): Unit", "member" : "scala.AnyRef.notifyAll", "link" : "turnordering\/AsyncDbManager.html#notifyAll():Unit", "kind" : "final def"}, {"label" : "notify", "tail" : "(): Unit", "member" : "scala.AnyRef.notify", "link" : "turnordering\/AsyncDbManager.html#notify():Unit", "kind" : "final def"}, {"label" : "toString", "tail" : "(): String", "member" : "scala.AnyRef.toString", "link" : "turnordering\/AsyncDbManager.html#toString():String", "kind" : "def"}, {"label" : "clone", "tail" : "(): AnyRef", "member" : "scala.AnyRef.clone", "link" : "turnordering\/AsyncDbManager.html#clone():Object", "kind" : "def"}, {"label" : "equals", "tail" : "(arg0: Any): Boolean", "member" : "scala.AnyRef.equals", "link" : "turnordering\/AsyncDbManager.html#equals(x$1:Any):Boolean", "kind" : "def"}, {"label" : "hashCode", "tail" : "(): Int", "member" : "scala.AnyRef.hashCode", "link" : "turnordering\/AsyncDbManager.html#hashCode():Int", "kind" : "def"}, {"label" : "getClass", "tail" : "(): Class[_]", "member" : "scala.AnyRef.getClass", "link" : "turnordering\/AsyncDbManager.html#getClass():Class[_]", "kind" : "final def"}, {"label" : "asInstanceOf", "tail" : "(): T0", "member" : "scala.Any.asInstanceOf", "link" : "turnordering\/AsyncDbManager.html#asInstanceOf[T0]:T0", "kind" : "final def"}, {"label" : "isInstanceOf", "tail" : "(): Boolean", "member" : "scala.Any.isInstanceOf", "link" : "turnordering\/AsyncDbManager.html#isInstanceOf[T0]:Boolean", "kind" : "final def"}, {"label" : "deletePlayerInfo", "tail" : "(playerName: String, battleId: String, round: Int): Future[Boolean]", "member" : "turnordering.AsyncDbManager.deletePlayerInfo", "link" : "turnordering\/AsyncDbManager.html#deletePlayerInfo(playerName:String,battleId:String,round:Int):scala.concurrent.Future[Boolean]", "kind" : "abstract def"}, {"label" : "getPlayerInfo", "tail" : "(playerName: String, battleId: String, round: Int): Future[Option[PlayerInfo]]", "member" : "turnordering.AsyncDbManager.getPlayerInfo", "link" : "turnordering\/AsyncDbManager.html#getPlayerInfo(playerName:String,battleId:String,round:Int):scala.concurrent.Future[Option[communication.turnordering.PlayerInfo]]", "kind" : "abstract def"}, {"label" : "addPlayerInfo", "tail" : "(playerInfo: PlayerInfo): Future[String]", "member" : "turnordering.AsyncDbManager.addPlayerInfo", "link" : "turnordering\/AsyncDbManager.html#addPlayerInfo(playerInfo:communication.turnordering.PlayerInfo):scala.concurrent.Future[String]", "kind" : "abstract def"}, {"label" : "incrementCurrentRound", "tail" : "(battleId: String): Future[Boolean]", "member" : "turnordering.AsyncDbManager.incrementCurrentRound", "link" : "turnordering\/AsyncDbManager.html#incrementCurrentRound(battleId:String):scala.concurrent.Future[Boolean]", "kind" : "abstract def"}, {"label" : "getCurrentRound", "tail" : "(battleId: String): Future[Int]", "member" : "turnordering.AsyncDbManager.getCurrentRound", "link" : "turnordering\/AsyncDbManager.html#getCurrentRound(battleId:String):scala.concurrent.Future[Int]", "kind" : "abstract def"}], "shortDescription" : "Provides functionality to manage the asynchronous interaction between theturn-ordering service and an asynchronous database.", "trait" : "turnordering\/AsyncDbManager.html", "kind" : "trait"}, {"name" : "turnordering.MongoDbManager", "shortDescription" : "Provides functionality to manage the asynchronous interaction between theturn-ordering service and a MongoDB database.", "object" : "turnordering\/MongoDbManager$.html", "members_object" : [{"label" : "deletePlayerInfo", "tail" : "(playerName: String, battleId: String, round: Int): Future[Boolean]", "member" : "turnordering.MongoDbManager.deletePlayerInfo", "link" : "turnordering\/MongoDbManager$.html#deletePlayerInfo(playerName:String,battleId:String,round:Int):scala.concurrent.Future[Boolean]", "kind" : "def"}, {"label" : "getPlayerInfo", "tail" : "(playerName: String, battleId: String, round: Int): Future[Option[PlayerInfo]]", "member" : "turnordering.MongoDbManager.getPlayerInfo", "link" : "turnordering\/MongoDbManager$.html#getPlayerInfo(playerName:String,battleId:String,round:Int):scala.concurrent.Future[Option[communication.turnordering.PlayerInfo]]", "kind" : "def"}, {"label" : "addPlayerInfo", "tail" : "(playerInfo: PlayerInfo): Future[String]", "member" : "turnordering.MongoDbManager.addPlayerInfo", "link" : "turnordering\/MongoDbManager$.html#addPlayerInfo(playerInfo:communication.turnordering.PlayerInfo):scala.concurrent.Future[String]", "kind" : "def"}, {"label" : "incrementCurrentRound", "tail" : "(battleId: String): Future[Boolean]", "member" : "turnordering.MongoDbManager.incrementCurrentRound", "link" : "turnordering\/MongoDbManager$.html#incrementCurrentRound(battleId:String):scala.concurrent.Future[Boolean]", "kind" : "def"}, {"label" : "getCurrentRound", "tail" : "(battleId: String): Future[Int]", "member" : "turnordering.MongoDbManager.getCurrentRound", "link" : "turnordering\/MongoDbManager$.html#getCurrentRound(battleId:String):scala.concurrent.Future[Int]", "kind" : "def"}, {"label" : "turnOrderingCollection", "tail" : ": MongoCollection[Document]", "member" : "turnordering.MongoDbManager.turnOrderingCollection", "link" : "turnordering\/MongoDbManager$.html#turnOrderingCollection:org.mongodb.scala.MongoCollection[org.mongodb.scala.Document]", "kind" : "val"}, {"label" : "battlesCollection", "tail" : ": MongoCollection[Document]", "member" : "turnordering.MongoDbManager.battlesCollection", "link" : "turnordering\/MongoDbManager$.html#battlesCollection:org.mongodb.scala.MongoCollection[org.mongodb.scala.Document]", "kind" : "val"}, {"label" : "database", "tail" : ": MongoDatabase", "member" : "turnordering.MongoDbManager.database", "link" : "turnordering\/MongoDbManager$.html#database:org.mongodb.scala.MongoDatabase", "kind" : "val"}, {"label" : "synchronized", "tail" : "(arg0: ⇒ T0): T0", "member" : "scala.AnyRef.synchronized", "link" : "turnordering\/MongoDbManager$.html#synchronized[T0](x$1:=>T0):T0", "kind" : "final def"}, {"label" : "##", "tail" : "(): Int", "member" : "scala.AnyRef.##", "link" : "turnordering\/MongoDbManager$.html###():Int", "kind" : "final def"}, {"label" : "!=", "tail" : "(arg0: Any): Boolean", "member" : "scala.AnyRef.!=", "link" : "turnordering\/MongoDbManager$.html#!=(x$1:Any):Boolean", "kind" : "final def"}, {"label" : "==", "tail" : "(arg0: Any): Boolean", "member" : "scala.AnyRef.==", "link" : "turnordering\/MongoDbManager$.html#==(x$1:Any):Boolean", "kind" : "final def"}, {"label" : "ne", "tail" : "(arg0: AnyRef): Boolean", "member" : "scala.AnyRef.ne", "link" : "turnordering\/MongoDbManager$.html#ne(x$1:AnyRef):Boolean", "kind" : "final def"}, {"label" : "eq", "tail" : "(arg0: AnyRef): Boolean", "member" : "scala.AnyRef.eq", "link" : "turnordering\/MongoDbManager$.html#eq(x$1:AnyRef):Boolean", "kind" : "final def"}, {"label" : "finalize", "tail" : "(): Unit", "member" : "scala.AnyRef.finalize", "link" : "turnordering\/MongoDbManager$.html#finalize():Unit", "kind" : "def"}, {"label" : "wait", "tail" : "(): Unit", "member" : "scala.AnyRef.wait", "link" : "turnordering\/MongoDbManager$.html#wait():Unit", "kind" : "final def"}, {"label" : "wait", "tail" : "(arg0: Long, arg1: Int): Unit", "member" : "scala.AnyRef.wait", "link" : "turnordering\/MongoDbManager$.html#wait(x$1:Long,x$2:Int):Unit", "kind" : "final def"}, {"label" : "wait", "tail" : "(arg0: Long): Unit", "member" : "scala.AnyRef.wait", "link" : "turnordering\/MongoDbManager$.html#wait(x$1:Long):Unit", "kind" : "final def"}, {"label" : "notifyAll", "tail" : "(): Unit", "member" : "scala.AnyRef.notifyAll", "link" : "turnordering\/MongoDbManager$.html#notifyAll():Unit", "kind" : "final def"}, {"label" : "notify", "tail" : "(): Unit", "member" : "scala.AnyRef.notify", "link" : "turnordering\/MongoDbManager$.html#notify():Unit", "kind" : "final def"}, {"label" : "toString", "tail" : "(): String", "member" : "scala.AnyRef.toString", "link" : "turnordering\/MongoDbManager$.html#toString():String", "kind" : "def"}, {"label" : "clone", "tail" : "(): AnyRef", "member" : "scala.AnyRef.clone", "link" : "turnordering\/MongoDbManager$.html#clone():Object", "kind" : "def"}, {"label" : "equals", "tail" : "(arg0: Any): Boolean", "member" : "scala.AnyRef.equals", "link" : "turnordering\/MongoDbManager$.html#equals(x$1:Any):Boolean", "kind" : "def"}, {"label" : "hashCode", "tail" : "(): Int", "member" : "scala.AnyRef.hashCode", "link" : "turnordering\/MongoDbManager$.html#hashCode():Int", "kind" : "def"}, {"label" : "getClass", "tail" : "(): Class[_]", "member" : "scala.AnyRef.getClass", "link" : "turnordering\/MongoDbManager$.html#getClass():Class[_]", "kind" : "final def"}, {"label" : "asInstanceOf", "tail" : "(): T0", "member" : "scala.Any.asInstanceOf", "link" : "turnordering\/MongoDbManager$.html#asInstanceOf[T0]:T0", "kind" : "final def"}, {"label" : "isInstanceOf", "tail" : "(): Boolean", "member" : "scala.Any.isInstanceOf", "link" : "turnordering\/MongoDbManager$.html#isInstanceOf[T0]:Boolean", "kind" : "final def"}], "kind" : "object"}, {"name" : "turnordering.TestConversion", "shortDescription" : "Naive test useful both as a usage example and as a verification for theconversion from PlayerInfo to Document and vice-versa.", "object" : "turnordering\/TestConversion$.html", "members_object" : [{"label" : "playerInfo", "tail" : ": PlayerInfo", "member" : "turnordering.TestConversion.playerInfo", "link" : "turnordering\/TestConversion$.html#playerInfo:communication.turnordering.PlayerInfo", "kind" : "val"}, {"label" : "clientQueue", "tail" : ": String", "member" : "turnordering.TestConversion.clientQueue", "link" : "turnordering\/TestConversion$.html#clientQueue:String", "kind" : "val"}, {"label" : "round", "tail" : ": Int", "member" : "turnordering.TestConversion.round", "link" : "turnordering\/TestConversion$.html#round:Int", "kind" : "val"}, {"label" : "battleId", "tail" : ": String", "member" : "turnordering.TestConversion.battleId", "link" : "turnordering\/TestConversion$.html#battleId:String", "kind" : "val"}, {"label" : "speeds", "tail" : ": Map[String, Int]", "member" : "turnordering.TestConversion.speeds", "link" : "turnordering\/TestConversion$.html#speeds:scala.collection.immutable.Map[String,Int]", "kind" : "val"}, {"label" : "playerName", "tail" : ": String", "member" : "turnordering.TestConversion.playerName", "link" : "turnordering\/TestConversion$.html#playerName:String", "kind" : "val"}, {"label" : "main", "tail" : "(args: Array[String]): Unit", "member" : "scala.App.main", "link" : "turnordering\/TestConversion$.html#main(args:Array[String]):Unit", "kind" : "def"}, {"label" : "delayedInit", "tail" : "(body: ⇒ Unit): Unit", "member" : "scala.App.delayedInit", "link" : "turnordering\/TestConversion$.html#delayedInit(body:=>Unit):Unit", "kind" : "def"}, {"label" : "args", "tail" : "(): Array[String]", "member" : "scala.App.args", "link" : "turnordering\/TestConversion$.html#args:Array[String]", "kind" : "def"}, {"label" : "executionStart", "tail" : ": Long", "member" : "scala.App.executionStart", "link" : "turnordering\/TestConversion$.html#executionStart:Long", "kind" : "val"}, {"label" : "synchronized", "tail" : "(arg0: ⇒ T0): T0", "member" : "scala.AnyRef.synchronized", "link" : "turnordering\/TestConversion$.html#synchronized[T0](x$1:=>T0):T0", "kind" : "final def"}, {"label" : "##", "tail" : "(): Int", "member" : "scala.AnyRef.##", "link" : "turnordering\/TestConversion$.html###():Int", "kind" : "final def"}, {"label" : "!=", "tail" : "(arg0: Any): Boolean", "member" : "scala.AnyRef.!=", "link" : "turnordering\/TestConversion$.html#!=(x$1:Any):Boolean", "kind" : "final def"}, {"label" : "==", "tail" : "(arg0: Any): Boolean", "member" : "scala.AnyRef.==", "link" : "turnordering\/TestConversion$.html#==(x$1:Any):Boolean", "kind" : "final def"}, {"label" : "ne", "tail" : "(arg0: AnyRef): Boolean", "member" : "scala.AnyRef.ne", "link" : "turnordering\/TestConversion$.html#ne(x$1:AnyRef):Boolean", "kind" : "final def"}, {"label" : "eq", "tail" : "(arg0: AnyRef): Boolean", "member" : "scala.AnyRef.eq", "link" : "turnordering\/TestConversion$.html#eq(x$1:AnyRef):Boolean", "kind" : "final def"}, {"label" : "finalize", "tail" : "(): Unit", "member" : "scala.AnyRef.finalize", "link" : "turnordering\/TestConversion$.html#finalize():Unit", "kind" : "def"}, {"label" : "wait", "tail" : "(): Unit", "member" : "scala.AnyRef.wait", "link" : "turnordering\/TestConversion$.html#wait():Unit", "kind" : "final def"}, {"label" : "wait", "tail" : "(arg0: Long, arg1: Int): Unit", "member" : "scala.AnyRef.wait", "link" : "turnordering\/TestConversion$.html#wait(x$1:Long,x$2:Int):Unit", "kind" : "final def"}, {"label" : "wait", "tail" : "(arg0: Long): Unit", "member" : "scala.AnyRef.wait", "link" : "turnordering\/TestConversion$.html#wait(x$1:Long):Unit", "kind" : "final def"}, {"label" : "notifyAll", "tail" : "(): Unit", "member" : "scala.AnyRef.notifyAll", "link" : "turnordering\/TestConversion$.html#notifyAll():Unit", "kind" : "final def"}, {"label" : "notify", "tail" : "(): Unit", "member" : "scala.AnyRef.notify", "link" : "turnordering\/TestConversion$.html#notify():Unit", "kind" : "final def"}, {"label" : "toString", "tail" : "(): String", "member" : "scala.AnyRef.toString", "link" : "turnordering\/TestConversion$.html#toString():String", "kind" : "def"}, {"label" : "clone", "tail" : "(): AnyRef", "member" : "scala.AnyRef.clone", "link" : "turnordering\/TestConversion$.html#clone():Object", "kind" : "def"}, {"label" : "equals", "tail" : "(arg0: Any): Boolean", "member" : "scala.AnyRef.equals", "link" : "turnordering\/TestConversion$.html#equals(x$1:Any):Boolean", "kind" : "def"}, {"label" : "hashCode", "tail" : "(): Int", "member" : "scala.AnyRef.hashCode", "link" : "turnordering\/TestConversion$.html#hashCode():Int", "kind" : "def"}, {"label" : "getClass", "tail" : "(): Class[_]", "member" : "scala.AnyRef.getClass", "link" : "turnordering\/TestConversion$.html#getClass():Class[_]", "kind" : "final def"}, {"label" : "asInstanceOf", "tail" : "(): T0", "member" : "scala.Any.asInstanceOf", "link" : "turnordering\/TestConversion$.html#asInstanceOf[T0]:T0", "kind" : "final def"}, {"label" : "isInstanceOf", "tail" : "(): Boolean", "member" : "scala.Any.isInstanceOf", "link" : "turnordering\/TestConversion$.html#isInstanceOf[T0]:Boolean", "kind" : "final def"}], "kind" : "object"}]};