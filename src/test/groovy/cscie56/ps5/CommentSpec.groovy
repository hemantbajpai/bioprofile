package cscie56.ps5

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Comment)
class CommentSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "comment should belong to blogentry"() {
        when:
            Comment comment = new Comment(text: "text", dateCreated:Date.parse("MM-dd-yyyy", "04-18-2016"), user: new User())
        then:
            !comment.validate()
        when:
            comment.blogEntry = new BlogEntry()
            comment.save(flush: true)
        then:
            comment.validate()
    }

    void "comment should have user"() {
        when:
        Comment comment = new Comment(text: "text", dateCreated:Date.parse("MM-dd-yyyy", "04-18-2016"), blogEntry: new BlogEntry())
        then:
        !comment.validate()
        when:
        comment.user = new User()
        comment.save(flush: true)
        then:
        comment.validate()
    }

    void "comment text should not be blank"() {
        when:
        Comment comment = new Comment(text: "", dateCreated:Date.parse("MM-dd-yyyy", "04-18-2016"), blogEntry: new BlogEntry(), user: new User())
        then:
        !comment.validate()
        when:
        comment.text = "text"
        comment.save(flush: true)
        then:
        comment.validate()
    }
}
