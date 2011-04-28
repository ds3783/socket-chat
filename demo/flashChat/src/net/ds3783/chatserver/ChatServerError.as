package net.ds3783.chatserver
{
	import mx.effects.easing.Exponential;
	
	public class ChatServerError extends Error
	{
		
		public function ChatServerError(message:String){
			this.message=message;
		}

	}
}