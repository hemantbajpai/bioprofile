package cscie56.ps5

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(BlogEntry)
class BlogEntrySpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "blogentry should belong to user"() {
        when:
            BlogEntry blogEntry = new BlogEntry(text: "text", published: true, dateCreated:Date.parse("MM-dd-yyyy", "04-15-2016"), datePublished:Date.parse("MM-dd-yyyy", "04-18-2016"))
        then:
            !blogEntry.validate()
        when:
            blogEntry.user = new User()
            blogEntry.save(flush: true)
        then:
            blogEntry.validate()
    }

    void "blogentry text should not be blank"() {
        when:
        BlogEntry blogEntry = new BlogEntry(text: "", published: true, dateCreated:Date.parse("MM-dd-yyyy", "04-15-2016"), datePublished:Date.parse("MM-dd-yyyy", "04-18-2016"), user: new User())
        then:
        !blogEntry.validate()
        when:
        blogEntry.text = "text"
        blogEntry.save(flush: true)
        then:
        blogEntry.validate()
    }

}
