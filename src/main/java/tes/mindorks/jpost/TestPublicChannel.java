package tes.mindorks.jpost;

import com.mindorks.javajpost.JPost;
import com.mindorks.jpost.core.Channel;
import com.mindorks.jpost.exceptions.AlreadyExistsException;
import com.mindorks.jpost.exceptions.JPostNotRunningException;

import tes.mindorks.jpost.message.Message1;
import tes.mindorks.jpost.message.Message2;
import tes.mindorks.jpost.subscriber.Subscriber;
import tes.mindorks.jpost.subscriber.SubscriberAsync;


/**
 * Created by janisharali on 30/09/16.
 */

public class TestPublicChannel {

    static Subscriber subscriberA;
    static Subscriber subscriberB;
    static SubscriberAsync subscriberAsyncA;
    static SubscriberAsync subscriberAsyncB;

    public static void test(){
        createChannel();
        testSubscriberSubscribeSync();
        testSubscriberBroadcastSync();
        testSubscriberBroadcastAsync();
        testShutdownRestartSync();
        testShutdownRestartAsync();
        testSubscriberSubscribeAsync();
        testRemoveSubscriber();
    }

    private static void createChannel(){
        try{
            JPost.getBroadcastCenter().createPublicChannel(ChannelIds.publicChannel1);
        }catch (AlreadyExistsException e){
            e.printStackTrace();
        }
    }

    private static void testSubscriberSubscribeSync(){
        System.out.println("testSubscriberSubscribeSync");
        subscriberA =  new Subscriber('A', ChannelIds.publicChannel1);
        subscriberB = new Subscriber('B', ChannelIds.publicChannel1);
    }

    private static void testSubscriberBroadcastSync(){
        System.out.println("testSubscriberBroadcastSync");
        Message1 message1 = new Message1("Public message subscriber async 1");
        Message2 message2 = new Message2("Public message subscriber async 2");
        JPost.getBroadcastCenter().broadcast(ChannelIds.publicChannel1, message1);
        JPost.getBroadcastCenter().broadcast(ChannelIds.publicChannel1, message2);
    }

    private static void testSubscriberBroadcastAsync(){

        System.out.println("testSubscriberBroadcastAsync");
        Message1 message1 = new Message1("Public message 1 async");
        Message2 message2 = new Message2("Public message 2 async");
        try {
            JPost.getBroadcastCenter().broadcastAsync(ChannelIds.publicChannel1, message1);
        }catch (JPostNotRunningException e){
            e.printStackTrace();
        }
        try {
            JPost.getBroadcastCenter().broadcastAsync(ChannelIds.publicChannel1, message2);
        }catch (JPostNotRunningException e){
            e.printStackTrace();
        }
    }

    private static void testSubscriberBroadcastSelectedSync(){
        System.out.println("testSubscriberBroadcastSync");
        Message1 message1 = new Message1("Public message subscriber async 1");
        Message2 message2 = new Message2("Public message subscriber async 2");
        JPost.getBroadcastCenter().broadcast(ChannelIds.publicChannel1, message1, subscriberA.hashCode());
        JPost.getBroadcastCenter().broadcast(ChannelIds.publicChannel1, message1, 1);
        JPost.getBroadcastCenter().broadcast(ChannelIds.publicChannel1, message2, 2);
    }

    private static void testSubscriberBroadcastSelectedAsync(){

        System.out.println("testSubscriberBroadcastAsync");
        Message1 message1 = new Message1("Public message 1 async");
        Message2 message2 = new Message2("Public message 2 async");
        try {
            JPost.getBroadcastCenter().broadcastAsync(ChannelIds.publicChannel1, message1, 1, 2);
        }catch (JPostNotRunningException e){
            e.printStackTrace();
        }
        try {
            JPost.getBroadcastCenter().broadcastAsync(ChannelIds.publicChannel1, message2, 1);
        }catch (JPostNotRunningException e){
            e.printStackTrace();
        }
    }

    private static void testShutdownRestartSync(){
        System.out.println("testShutdownRestartSync");
        JPost.shutdown();
        JPost.getBroadcastCenter().broadcast(ChannelIds.publicChannel1, new Message1("Public message sync 1 after shutdown"));
        JPost.getBroadcastCenter().broadcast(new Message2("Public message sync 2 after shutdown"));
        JPost.reboot();
        JPost.getBroadcastCenter().broadcast(ChannelIds.publicChannel1, new Message1("Public message sync 1 after reboot"));
        JPost.getBroadcastCenter().broadcast(ChannelIds.publicChannel1, new Message2("Public message sync 2 after reboot"));
        JPost.reboot();
    }

    private static void testShutdownRestartAsync(){
        System.out.println("testShutdownRestartAsync");
        JPost.shutdown();
        try {
            JPost.getBroadcastCenter().broadcastAsync(ChannelIds.publicChannel1, new Message1("Public message async 1 after shutdown"));
        }catch (JPostNotRunningException e){
            e.printStackTrace();
        }

        try {
            JPost.getBroadcastCenter().broadcastAsync(ChannelIds.publicChannel1, new Message2("Public message async 2 after shutdown"));
        }catch (JPostNotRunningException e){
            e.printStackTrace();
        }

        JPost.reboot();

        try {
            JPost.getBroadcastCenter().broadcastAsync(ChannelIds.publicChannel1, new Message1("Public message async 1 after reboot"));
        }catch (JPostNotRunningException e){
            e.printStackTrace();
        }

        try {
            JPost.getBroadcastCenter().broadcastAsync(ChannelIds.publicChannel1, new Message2("Public message async 2 after reboot"));
        }catch (JPostNotRunningException e){
            e.printStackTrace();
        }
    }

    private static void testSubscriberSubscribeAsync(){
        System.out.println("testSubscriberSubscribeAsync");
        subscriberAsyncA =  new SubscriberAsync('A', ChannelIds.publicChannel1);
        subscriberAsyncB = new SubscriberAsync('B', ChannelIds.publicChannel1);
    }

    private static void testRemoveSubscriber(){
        System.out.println("removeSubscriberTest");
        try {
            JPost.getBroadcastCenter().removeSubscriber(ChannelIds.publicChannel1, subscriberA);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            JPost.getBroadcastCenter().removeSubscriber(ChannelIds.publicChannel1, subscriberB);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            JPost.getBroadcastCenter().removeSubscriber(ChannelIds.publicChannel1, subscriberAsyncA);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            JPost.getBroadcastCenter().broadcastAsync(ChannelIds.publicChannel1, new Message1("Public message async 1 after remove subscriber"));
        }catch (JPostNotRunningException e){
            e.printStackTrace();
        }

        try {
            JPost.getBroadcastCenter().broadcastAsync(ChannelIds.publicChannel1, new Message2("Public message async 2 after remove subscriber"));
        }catch (JPostNotRunningException e){
            e.printStackTrace();
        }
    }

    private static void testChannelStateAsync(){
        System.out.println("testChannelStateAsync");
        JPost.getBroadcastCenter().stopChannel(ChannelIds.publicChannel1);
        try {
            JPost.getBroadcastCenter().broadcastAsync(ChannelIds.publicChannel1, new Message1("Public message async 1 after shutdown"));
        }catch (JPostNotRunningException e){
            e.printStackTrace();
        }

        try {
            JPost.getBroadcastCenter().broadcastAsync(ChannelIds.publicChannel1, new Message2("Public message async 2 after shutdown"));
        }catch (JPostNotRunningException e){
            e.printStackTrace();
        }

        JPost.getBroadcastCenter().reopenChannel(ChannelIds.publicChannel1);

        try {
            JPost.getBroadcastCenter().broadcastAsync(ChannelIds.publicChannel1, new Message1("Public message async 1 after reboot"));
        }catch (JPostNotRunningException e){
            e.printStackTrace();
        }

        try {
            JPost.getBroadcastCenter().broadcastAsync(ChannelIds.publicChannel1, new Message2("Public message async 2 after reboot"));
        }catch (JPostNotRunningException e){
            e.printStackTrace();
        }

        JPost.getBroadcastCenter().terminateChannel(ChannelIds.publicChannel1);

        try {
            JPost.getBroadcastCenter().broadcastAsync(ChannelIds.publicChannel1, new Message1("Public message async 1 after reboot"));
        }catch (JPostNotRunningException e){
            e.printStackTrace();
        }

        try {
            JPost.getBroadcastCenter().broadcastAsync(ChannelIds.publicChannel1, new Message2("Public message async 2 after reboot"));
        }catch (JPostNotRunningException e){
            e.printStackTrace();
        }
    }
}
