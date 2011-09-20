package net.ds3783.chatserver
{

	public class Message
	{
		public var userUuid:String;
		public var id:String;
		public var type:String;
		public var channel:String;
		public var destUid:String;
		public var content:String;
		public var authCode:String;
		
        
        public var destUserUids:Array;
        public var dropClientAfterReply:Boolean;
		
		
		
		
		
		
		public function Message()
		{
			
		}

	}
}