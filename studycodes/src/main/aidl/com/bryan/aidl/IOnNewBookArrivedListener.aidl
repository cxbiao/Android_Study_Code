// IOnNewBookArrivedListener.aidl
package com.bryan.aidl;

import com.bryan.aidl.Book;

interface IOnNewBookArrivedListener {
   void onNewBookArrived(in Book newBook);

}
