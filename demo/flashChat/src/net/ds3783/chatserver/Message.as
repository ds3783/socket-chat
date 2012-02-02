/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-2-2
 * Time: ����11:32
 *
 */
package net.ds3783.chatserver {
/**
 *  ��Ϣ�ӿڣ�����ͨ�ŵ���Ϣ����ʵ�ִ˽ӿ�
 */
public interface Message {
    /**
     * @return ��Ϣ���ͣ����ڱ�ʶ����Ϣ�ľ�������
     */
    function getType():String;

    /**
     * @return �Ƿ�����л�
     */
    function isSerializable():Boolean;

    /**
     * @return ��Ϣ�����ݣ�ĳЩЭ��Բ������л�����Ϣ��ʹ����������Ϊ��������
     */
    function get content():String;

    function set content(value:String):void;
}
}
