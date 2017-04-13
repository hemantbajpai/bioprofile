package cscie56.ps5

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured([Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_ADMIN])
class BlogEntryController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def springSecurityService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond BlogEntry.list(params), model:[blogEntryCount: BlogEntry.count()]
    }

    @Secured(Role.ROLE_USER)
    def publishBlog() {

        def user = User.get(springSecurityService.principal.id)

        def blogEntry = new BlogEntry(text:params.text, dateCreated:new Date(), datePublished:new Date(), published:true, user:user, comments:[])
        blogEntry.save(flush:true, failOnError:true)

        user.blogEntries.add(blogEntry)
        user.save(flush:true, failOnError:true)
        redirect(uri: request.getHeader('referer') )
    }

    @Secured(Role.ROLE_USER)
    def saveBlog() {

        def user = User.get(springSecurityService.principal.id)

        def blogEntry = new BlogEntry(text:params.text, dateCreated:new Date(), datePublished:new Date(), published:false, user:user, comments:[])
        blogEntry.save(flush:true, failOnError:true)

        user.blogEntries.add(blogEntry)
        user.save(flush:true, failOnError:true)
        redirect(uri: request.getHeader('referer') )
    }

    @Secured(Role.ROLE_USER)
    def publishEntry() {

        def blogEntry = BlogEntry.get(params.blogId)

        blogEntry.published = true
        blogEntry.save(flush:true, failOnError:true)
        redirect(uri: request.getHeader('referer') )
    }

    @Secured(Role.ROLE_USER)
    def reject (Comment comment) {

        comment.approved = false;
        comment.pending = false;
        comment.save(flush:true, failOnError:true)
        render comment as JSON
    }

    @Secured(Role.ROLE_USER)
    def approved (Comment comment) {

        comment.approved = true;
        comment.pending = false;
        comment.save(flush:true, failOnError:true)
        render comment as JSON
    }

    @Secured(Role.ROLE_USER)
    def addComment() {

        def blogEntry = BlogEntry.get(params.blogId)

        def user = User.get(springSecurityService.principal.id)
        def commentToAdd = new Comment(text:params.text, dateCreated:new Date(), blogEntry:blogEntry, user: user, approved:false, pending:true)
        commentToAdd.save(flush:true, failOnError:true)

        blogEntry.comments.add(commentToAdd)
        blogEntry.save(flush:true, failOnError:true)
        redirect(uri: request.getHeader('referer') )
    }


    def show(BlogEntry blogEntry) {
        respond blogEntry
    }

    def create() {
        respond new BlogEntry(params)
    }

    @Transactional
    def save(BlogEntry blogEntry) {
        if (blogEntry == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (blogEntry.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond blogEntry.errors, view:'create'
            return
        }

        blogEntry.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'blogEntry.label', default: 'BlogEntry'), blogEntry.id])
                redirect blogEntry
            }
            '*' { respond blogEntry, [status: CREATED] }
        }
    }

    def edit(BlogEntry blogEntry) {
        respond blogEntry
    }

    @Transactional
    def update(BlogEntry blogEntry) {
        if (blogEntry == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (blogEntry.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond blogEntry.errors, view:'edit'
            return
        }

        blogEntry.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'blogEntry.label', default: 'BlogEntry'), blogEntry.id])
                redirect blogEntry
            }
            '*'{ respond blogEntry, [status: OK] }
        }
    }

    @Transactional
    def delete(BlogEntry blogEntry) {

        if (blogEntry == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        blogEntry.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'blogEntry.label', default: 'BlogEntry'), blogEntry.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'blogEntry.label', default: 'BlogEntry'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
