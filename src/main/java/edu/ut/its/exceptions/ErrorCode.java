package edu.ut.its.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    // Not Found (1000-1099)
    ACCOUNT_NOT_FOUND(1000, "Account not found", HttpStatus.NOT_FOUND),
    ACCOUNT_ROLE_NOT_FOUND(1001, "Account role not found", HttpStatus.NOT_FOUND),
    ACCOUNT_EMAIL_NOT_FOUND(1002, "Email not found", HttpStatus.NOT_FOUND),

    // Already Exists (1100-1199)
    ACCOUNT_ALREADY_EXISTS(1100, "Account already exists", HttpStatus.CONFLICT),
    ACCOUNT_EMAIL_ALREADY_EXISTS(1101, "Account with email already exists", HttpStatus.CONFLICT),
    ACCOUNT_USERNAME_ALREADY_EXISTS(1102, "Account with username already exists", HttpStatus.CONFLICT),

    // Validation (1200-1299)
    ACCOUNT_INVALID_CREDENTIALS(1200, "Invalid account credentials", HttpStatus.BAD_REQUEST),
    ACCOUNT_PASSWORD_INVALID_FORMAT(1201, "Password format is invalid", HttpStatus.BAD_REQUEST),
    ACCOUNT_EMAIL_INVALID_FORMAT(1202, "Email format is invalid", HttpStatus.BAD_REQUEST),
    ACCOUNT_USERNAME_INVALID_FORMAT(1203, "Username format is invalid", HttpStatus.BAD_REQUEST),
    ACCOUNT_PASSWORD_INVALID(1204, "Password is invalid", HttpStatus.BAD_REQUEST),

    // Permission (1300-1399)
    ACCOUNT_INSUFFICIENT_PRIVILEGES(1300, "Insufficient account privileges", HttpStatus.FORBIDDEN),
    ACCOUNT_LOCKED(1301, "Account is locked", HttpStatus.FORBIDDEN),
    ACCOUNT_DISABLED(1302, "Account is disabled", HttpStatus.FORBIDDEN),

    // System (1400-1499)
    ACCOUNT_AUTHENTICATION_FAILED(1400, "Authentication failed", HttpStatus.UNAUTHORIZED),
    ACCOUNT_AUTHENTICATION_NOT_FOUND(1402, "Authentication not found", HttpStatus.UNAUTHORIZED),
    ACCOUNT_SESSION_EXPIRED(1401, "Account session expired", HttpStatus.UNAUTHORIZED),

    // UploadFile(1500 - 1599)
    FILE_UPLOAD_NOT_FOUND(1500, "File upload not found", HttpStatus.BAD_REQUEST),

    // ================ CAMERA ERROR CODES (2000-2999) ================
    // Not Found (2000-2099)
    CAMERA_NOT_FOUND(2000, "Camera not found", HttpStatus.NOT_FOUND),
    CAMERA_STREAM_NOT_FOUND(2001, "Camera stream not found", HttpStatus.NOT_FOUND),

    // Already Exists (2100-2199)
    CAMERA_ALREADY_EXISTS(2100, "Camera already exists", HttpStatus.CONFLICT),
    CAMERA_SERIAL_ALREADY_EXISTS(2101, "Camera with serial number already exists", HttpStatus.CONFLICT),

    // Validation (2200-2299)
    CAMERA_INVALID_CONFIGURATION(2200, "Invalid camera configuration", HttpStatus.BAD_REQUEST),
    CAMERA_INVALID_STREAM_URL(2201, "Invalid camera stream URL", HttpStatus.BAD_REQUEST),
    CAMERA_INVALID_POSITION(2202, "Invalid camera position", HttpStatus.BAD_REQUEST),

    // Connection (2300-2399)
    CAMERA_CONNECTION_FAILED(2300, "Failed to connect to camera", HttpStatus.SERVICE_UNAVAILABLE),
    CAMERA_STREAM_INTERRUPTED(2301, "Camera stream interrupted", HttpStatus.SERVICE_UNAVAILABLE),
    CAMERA_OFFLINE(2302, "Camera is offline", HttpStatus.SERVICE_UNAVAILABLE),

    // System (2400-2499)
    CAMERA_HARDWARE_ERROR(2400, "Camera hardware error", HttpStatus.INTERNAL_SERVER_ERROR),
    CAMERA_SOFTWARE_ERROR(2401, "Camera software error", HttpStatus.INTERNAL_SERVER_ERROR),

    // ================ STREET ERROR CODES (3000-3999) ================
    // Not Found (3000-3099)
    STREET_NOT_FOUND(3000, "Street not found", HttpStatus.NOT_FOUND),
    STREET_ZONE_NOT_FOUND(3001, "Street zone not found", HttpStatus.NOT_FOUND),
    STREET_SEGMENT_NOT_FOUND(3002, "Street segment not found", HttpStatus.NOT_FOUND),

    // Already Exists (3100-3199)
    STREET_ALREADY_EXISTS(3100, "Street already exists", HttpStatus.CONFLICT),
    STREET_CODE_ALREADY_EXISTS(3101, "Street with code already exists", HttpStatus.CONFLICT),
    STREET_NAME_ALREADY_EXISTS(31002, "Street with name already exists", HttpStatus.CONFLICT),

    // Validation (3200-3299)
    STREET_INVALID_COORDINATES(3200, "Invalid street coordinates", HttpStatus.BAD_REQUEST),
    STREET_INVALID_LENGTH(3201, "Invalid street length", HttpStatus.BAD_REQUEST),
    STREET_INVALID_ZONE_ASSIGNMENT(3202, "Invalid street zone assignment", HttpStatus.BAD_REQUEST),

    // System (3400-3499)
    STREET_MAPPING_ERROR(3400, "Street mapping error", HttpStatus.INTERNAL_SERVER_ERROR),
    STREET_DATA_INCONSISTENCY(3401, "Street data inconsistency", HttpStatus.INTERNAL_SERVER_ERROR),

    // ================ STREET LOG ERROR CODES (4000-4999) ================
    // Not Found (4000-4099)
    STREET_LOG_NOT_FOUND(4000, "Street log not found", HttpStatus.NOT_FOUND),
    STREET_LOG_ENTRY_NOT_FOUND(4001, "Street log entry not found", HttpStatus.NOT_FOUND),

    // Already Exists (4100-4199)
    STREET_LOG_ALREADY_EXISTS(4100, "Street log already exists", HttpStatus.CONFLICT),
    STREET_LOG_DUPLICATE_ENTRY(4101, "Duplicate street log entry", HttpStatus.CONFLICT),

    // Validation (4200-4299)
    STREET_LOG_INVALID_TIMESTAMP(4200, "Invalid street log timestamp", HttpStatus.BAD_REQUEST),
    STREET_LOG_INVALID_DATA(4201, "Invalid street log data", HttpStatus.BAD_REQUEST),
    STREET_LOG_INCONSISTENT_SEQUENCE(4202, "Inconsistent street log sequence", HttpStatus.BAD_REQUEST),

    // System (4400-4499)
    STREET_LOG_PROCESSING_ERROR(4400, "Street log processing error", HttpStatus.INTERNAL_SERVER_ERROR),
    STREET_LOG_STORAGE_ERROR(4401, "Street log storage error", HttpStatus.INTERNAL_SERVER_ERROR),

    // ================ VEHICLE ERROR CODES (5000-5999) ================
    // Not Found (5000-5099)
    VEHICLE_NOT_FOUND(5000, "Vehicle not found", HttpStatus.NOT_FOUND),
    VEHICLE_TYPE_NOT_FOUND(5001, "Vehicle type not found", HttpStatus.NOT_FOUND),
    VEHICLE_OWNER_NOT_FOUND(5002, "Vehicle owner not found", HttpStatus.NOT_FOUND),

    // Already Exists (5100-5199)
    VEHICLE_ALREADY_EXISTS(5100, "Vehicle already exists", HttpStatus.CONFLICT),
    VEHICLE_LICENSE_PLATE_ALREADY_EXISTS(5101, "Vehicle with license plate already exists", HttpStatus.CONFLICT),

    // Validation (5200-5299)
    VEHICLE_INVALID_LICENSE_PLATE(5200, "Invalid vehicle license plate", HttpStatus.BAD_REQUEST),
    VEHICLE_INVALID_TYPE(5201, "Invalid vehicle type", HttpStatus.BAD_REQUEST),
    VEHICLE_INVALID_REGISTRATION_DATA(5202, "Invalid vehicle registration data", HttpStatus.BAD_REQUEST),

    // System (5400-5499)
    VEHICLE_RECOGNITION_ERROR(5400, "Vehicle recognition error", HttpStatus.INTERNAL_SERVER_ERROR),
    VEHICLE_DATA_PROCESSING_ERROR(5401, "Vehicle data processing error", HttpStatus.INTERNAL_SERVER_ERROR),

    // ================ VIOLATION ERROR CODES (6000-6999) ================
    // Not Found (6000-6099)
    VIOLATION_NOT_FOUND(6000, "Violation not found", HttpStatus.NOT_FOUND),
    VIOLATION_TYPE_NOT_FOUND(6001, "Violation type not found", HttpStatus.NOT_FOUND),
    VIOLATION_EVIDENCE_NOT_FOUND(6002, "Violation evidence not found", HttpStatus.NOT_FOUND),

    // Already Exists (6100-6199)
    VIOLATION_ALREADY_EXISTS(6100, "Violation already exists", HttpStatus.CONFLICT),
    VIOLATION_DUPLICATE_ENTRY(6101, "Duplicate violation entry", HttpStatus.CONFLICT),

    // Validation (6200-6299)
    VIOLATION_INVALID_TIMESTAMP(6200, "Invalid violation timestamp", HttpStatus.BAD_REQUEST),
    VIOLATION_INVALID_EVIDENCE(6201, "Invalid violation evidence", HttpStatus.BAD_REQUEST),
    VIOLATION_INVALID_CLASSIFICATION(6202, "Invalid violation classification", HttpStatus.BAD_REQUEST),

    // Processing (6300-6399)
    VIOLATION_PROCESSING_FAILED(6300, "Violation processing failed", HttpStatus.UNPROCESSABLE_ENTITY),
    VIOLATION_VERIFICATION_FAILED(6301, "Violation verification failed", HttpStatus.UNPROCESSABLE_ENTITY),

    // System (6400-6499)
    VIOLATION_DETECTION_ERROR(6400, "Violation detection error", HttpStatus.INTERNAL_SERVER_ERROR),
    VIOLATION_REPORTING_ERROR(6401, "Violation reporting error", HttpStatus.INTERNAL_SERVER_ERROR),

    // ================ GENERAL ERROR CODES (9000-9999) ================
    // Validation (9200-9299)
    VALIDATION_ERROR(9200, "Validation error", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST_PARAMETER(9201, "Invalid request parameter", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_FIELD(9202, "Missing required field", HttpStatus.BAD_REQUEST),

    // Authentication/Authorization (9300-9399)
    AUTHENTICATION_ERROR(9300, "Authentication error", HttpStatus.UNAUTHORIZED),
    AUTHORIZATION_ERROR(9301, "Authorization error", HttpStatus.FORBIDDEN),
    FORBIDDEN_ACCESS(9302, "Forbidden access", HttpStatus.FORBIDDEN),

    // System (9400-9499)
    SYSTEM_ERROR(9400, "System error", HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_ERROR(9401, "Database error", HttpStatus.INTERNAL_SERVER_ERROR),
    NETWORK_ERROR(9402, "Network error", HttpStatus.SERVICE_UNAVAILABLE),

    // External Services (9500-9599)
    EXTERNAL_SERVICE_ERROR(9500, "External service error", HttpStatus.SERVICE_UNAVAILABLE),
    EXTERNAL_SERVICE_TIMEOUT(9501, "External service timeout", HttpStatus.GATEWAY_TIMEOUT),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
