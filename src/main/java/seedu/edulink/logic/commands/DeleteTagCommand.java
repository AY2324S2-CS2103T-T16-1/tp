package seedu.edulink.logic.commands;

import static seedu.edulink.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.edulink.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.edulink.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.edulink.commons.util.ToStringBuilder;
import seedu.edulink.logic.commands.exceptions.CommandException;
import seedu.edulink.model.Model;
import seedu.edulink.model.student.Id;
import seedu.edulink.model.student.Student;
import seedu.edulink.model.tag.Tag;

/**
 * Removes tags from a student.
 */
public class DeleteTagCommand extends Command {

    public static final String COMMAND_WORD = "dtag";

    public static final String MESSAGE_PERSON_NOTFOUND = "Can't find the person you specified.";
    public static final String MESSAGE_DELETE_TAG_SUCCESS = "Deleted Tags: %1$s";
    public static final String MESSAGE_USAGE = "Usage: " + COMMAND_WORD + " " + PREFIX_ID + "ID " + PREFIX_TAG + "Tag";
    public static final String MESSAGE_TAG_NOT_FOUND = "Invalid Command"
            + ", one or more tags you are looking for are not found.";

    private final Id studentToEditId;
    private final Set<Tag> tags;

    /**
     * Creates a DeleteTagCommand to delete tags from a student.
     *
     * @param studentToEditId the ID of the student user add tags to.
     * @param tags           a set of tags that the user wish to delete from the student.
     */
    public DeleteTagCommand(Id studentToEditId, Set<Tag> tags) {
        requireAllNonNull(studentToEditId, tags);

        this.studentToEditId = studentToEditId;
        this.tags = tags;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Student> lastShownList = model.getFilteredPersonList();
        Optional<Student> optionalStudentToEdit = lastShownList.stream().filter(
                student -> student.getId().equals(studentToEditId)
        ).findFirst();
        if (optionalStudentToEdit.isEmpty()) {
            throw new CommandException(MESSAGE_PERSON_NOTFOUND);
        }
        Student studentToEdit = optionalStudentToEdit.get();
        Set<Tag> originalTags = studentToEdit.getTags();
        Set<Tag> removedSet = new HashSet<>(originalTags);
        boolean isAllContained = originalTags.containsAll(tags);
        if (!isAllContained) {
            throw new CommandException(MESSAGE_TAG_NOT_FOUND);
        }
        removedSet.removeAll(tags);

        Student editedStudent = new Student(studentToEdit.getId(), studentToEdit.getMajor(), studentToEdit.getIntake(),
            studentToEdit.getGrade(), studentToEdit.getName(), studentToEdit.getPhone(), studentToEdit.getEmail(),
            studentToEdit.getAddress(), removedSet);

        model.setPerson(studentToEdit, editedStudent);
        return new CommandResult(String.format(MESSAGE_DELETE_TAG_SUCCESS, tags));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteTagCommand)) {
            return false;
        }

        DeleteTagCommand otherTagCommand = (DeleteTagCommand) other;
        boolean isStudentIdEqual = this.studentToEditId.equals(otherTagCommand.studentToEditId);
        boolean isTagListEqual = this.tags.equals(otherTagCommand.tags);
        return (isStudentIdEqual && isTagListEqual);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("Id", this.studentToEditId)
            .add("Tags", this.tags)
            .toString();
    }
}
