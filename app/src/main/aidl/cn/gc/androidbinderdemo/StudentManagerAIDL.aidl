// StudentManagerAIDL.aidl
package cn.gc.androidbinderdemo;

import cn.gc.androidbinderdemo.Student;
// Declare any non-default types here with import statements

interface StudentManagerAIDL {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
    void addStudent(in Student student);
    List<Student> getStudentList();
}
