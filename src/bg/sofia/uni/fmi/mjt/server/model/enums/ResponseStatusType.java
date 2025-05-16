package bg.sofia.uni.fmi.mjt.server.model.enums;

/**
 *
 * types of response that the service can send to the client :
 *  1. The request is okay + results from it (THE MESSAGE WILL BE IMPORTANT FOR THE CLIENT - it will display all
 *  fetch foods, there will be at least one food for the get-food command and one food for the get-food-report command)
 *  2. The request was not okay due to your fault or service fault, in both cases everything will be logged and
 *  the client will see a proper message (THE MESSAGE WILL BE IMPORTANT FOR THE CLIENT - it will explain the error)
 *  3. The request is okay but the searched item - with id or keywords will be missing. The message will be empty.
 *  (The client should interpreter it with a user-friendly message that the item/s searched is/are not available).
 */
public enum ResponseStatusType {
    OK,
    ERROR,
    NOT_FOUND
}
