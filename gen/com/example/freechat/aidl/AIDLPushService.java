/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/zhangwei/java/workspace2/FreeChat/FreeChat/src/com/example/freechat/aidl/AIDLPushService.aidl
 */
package com.example.freechat.aidl;
public interface AIDLPushService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.example.freechat.aidl.AIDLPushService
{
private static final java.lang.String DESCRIPTOR = "com.example.freechat.aidl.AIDLPushService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.example.freechat.aidl.AIDLPushService interface,
 * generating a proxy if needed.
 */
public static com.example.freechat.aidl.AIDLPushService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.example.freechat.aidl.AIDLPushService))) {
return ((com.example.freechat.aidl.AIDLPushService)iin);
}
return new com.example.freechat.aidl.AIDLPushService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_sendMessage:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.sendMessage(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_registerToPushService:
{
data.enforceInterface(DESCRIPTOR);
com.example.freechat.aidl.AIDLChatActivity _arg0;
_arg0 = com.example.freechat.aidl.AIDLChatActivity.Stub.asInterface(data.readStrongBinder());
this.registerToPushService(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.example.freechat.aidl.AIDLPushService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public boolean sendMessage(java.lang.String message) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(message);
mRemote.transact(Stub.TRANSACTION_sendMessage, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void registerToPushService(com.example.freechat.aidl.AIDLChatActivity callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerToPushService, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_sendMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_registerToPushService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public boolean sendMessage(java.lang.String message) throws android.os.RemoteException;
public void registerToPushService(com.example.freechat.aidl.AIDLChatActivity callback) throws android.os.RemoteException;
}
