package cscie56.ps5

class Comment {

    String text
    Date dateCreated

    boolean approved
    boolean pending

    User user

    static belongsTo = [blogEntry:BlogEntry]
    static hasMany = [replies:Reply]

    static constraints = {
        text blank: false
    }
}
