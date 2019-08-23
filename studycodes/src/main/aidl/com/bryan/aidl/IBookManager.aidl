// IBookManager.aidl
package com.bryan.aidl;
//除了基本类型外，其他类型都要加上in、out或者 inout参数
import com.bryan.aidl.Book;
import com.bryan.aidl.IOnNewBookArrivedListener;


interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
