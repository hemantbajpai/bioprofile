package cscie56.ps5

class Reply {

    String text
    Date dateCreated

    boolean approved
    boolean pending

    User user

    static belongsTo = [comment:Comment]
    static constraints = {
        text blank: false
    }
}
