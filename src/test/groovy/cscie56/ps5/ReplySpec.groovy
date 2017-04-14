package cscie56.ps5

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Reply)
class ReplySpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "reply should belong to comment"() {
        when:
            Reply reply = new Reply(text: "text", dateCreated:Date.parse("MM-dd-yyyy", "04-18-2016"), user: new User())
        then:
            !reply.validate()
        when:
            reply.comment = new Comment()
            reply.save(flush: true)
        then:
            reply.validate()
    }

    void "reply should have user"() {
        when:
        Reply reply = new Reply(text: "text", dateCreated:Date.parse("MM-dd-yyyy", "04-18-2016"), comment: new Comment())
        then:
        !reply.validate()
        when:
        reply.user = new User()
        reply.save(flush: true)
        then:
        reply.validate()
    }

    void "reply text should not be blank"() {
        when:
        Reply reply = new Reply(text: "", dateCreated:Date.parse("MM-dd-yyyy", "04-18-2016"), comment: new Comment(), user: new User())
        then:
        !reply.validate()
        when:
        reply.text = "text"
        reply.save(flush: true)
        then:
        reply.validate()
    }
}
