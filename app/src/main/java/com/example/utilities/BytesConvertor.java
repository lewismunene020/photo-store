package com.example.utilities;

public class BytesConvertor {
    public static final int BYTES_TO_KILO_BYTES = 1024;
    public static final int BYTES_TO_MEGA_BYTES = 1024 * BYTES_TO_KILO_BYTES;
    public static final int BYTES_TO_GIGA_BYTES = 1024 * BYTES_TO_MEGA_BYTES;

    public String convertToProgressString(long bytesTransferred, long totalByteCount) {
        String uploadString;
        if (totalByteCount < BYTES_TO_KILO_BYTES) {
            //convert to  bytes
            uploadString = bytesTransferred + "B / " + totalByteCount + "B";
        } else if (totalByteCount > BYTES_TO_KILO_BYTES && totalByteCount < BYTES_TO_MEGA_BYTES) {
            //convert to kilo bytes
            uploadString = convertBytesToUnits(bytesTransferred) + " / " + (totalByteCount / BYTES_TO_KILO_BYTES) + "KB";
        } else if (totalByteCount > BYTES_TO_MEGA_BYTES && totalByteCount < BYTES_TO_GIGA_BYTES) {
            //convert to  mega bytes
            uploadString = convertBytesToUnits(bytesTransferred) + " / " + (totalByteCount / BYTES_TO_MEGA_BYTES) + "MB";
        } else if (totalByteCount >= BYTES_TO_GIGA_BYTES) {
            uploadString = convertBytesToUnits(bytesTransferred) + " / " + (totalByteCount / BYTES_TO_GIGA_BYTES) + "GB";
        } else {
            //convert to  terabytes
            uploadString = "infinity";
        }
        return uploadString;
    }

    private String convertBytesToUnits(long bytesTransferred) {
        String bytesString = "";
        if (bytesTransferred < BYTES_TO_KILO_BYTES) {
            //convert to  bytes
            bytesString = bytesTransferred + "B";
        } else if (bytesTransferred > BYTES_TO_KILO_BYTES && bytesTransferred < BYTES_TO_MEGA_BYTES) {
            //convert to kilo bytes
            bytesString = (bytesTransferred / BYTES_TO_KILO_BYTES) + "KB";
        } else if (bytesTransferred > BYTES_TO_MEGA_BYTES && bytesTransferred < BYTES_TO_GIGA_BYTES) {
            //convert to  mega bytes
            bytesString = (bytesTransferred / BYTES_TO_MEGA_BYTES) + "MB";
        } else if (bytesTransferred >= BYTES_TO_GIGA_BYTES) {
            bytesString = (bytesTransferred / BYTES_TO_GIGA_BYTES) + "GB";
        }
        return bytesString;
    }
}
