/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

import java.util.function.Function;

/**
 *
 * @author ASUS
 */
public class EnumUtils {
    //Function<input taken, output produced> method (argument will be a method reference)
    //args: classtype, method reference, string, msg 
    //CANNOT USE SINCE NO MORE INTERFACE
//    public static <E extends Enum<E> & StatusEnumConvertible> E convertFromStatusLog(Class<E> enumType, String log, String msg) {
//        for (E constant : enumType.getEnumConstants()) {
//            if (constant.getStatusLog().equalsIgnoreCase(log)) {
//                return constant;
//            }
//        }
//        throw new IllegalArgumentException("Invalid " + msg + ": " + log);
//    }
    
    //for other enums using lambda approach
    public static <E extends Enum<E>> E convertFromLog(Class<E> enumType, Function<E, String> logGetter, String log, String msg) {
        for (E constant : enumType.getEnumConstants()) {
            if (logGetter.apply(constant).equalsIgnoreCase(log)) {
                return constant;
            }
        }
        throw new IllegalArgumentException("Invalid " + msg + ": " + log);
    }
}
