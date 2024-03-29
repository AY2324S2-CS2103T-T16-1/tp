package seedu.edulink.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.edulink.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.edulink.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.edulink.logic.commands.TagCommand.MESSAGE_ADD_TAG_SUCCESS;
import static seedu.edulink.logic.commands.TagCommand.MESSAGE_DUPLICATE;
import static seedu.edulink.testutil.TypicalPersons.ALICE;
import static seedu.edulink.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.edulink.model.Model;
import seedu.edulink.model.ModelManager;
import seedu.edulink.model.UserPrefs;
import seedu.edulink.model.grade.Course;
import seedu.edulink.model.grade.Grade;
import seedu.edulink.model.grade.Score;
import seedu.edulink.model.student.Id;
import seedu.edulink.model.student.Student;
import seedu.edulink.model.tag.Tag;
import seedu.edulink.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class TagCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        Id firstStudentId = new Id("A1029384A");
        HashSet<Tag> firstTagList = new HashSet<Tag>();
        firstTagList.add(new Tag("TopStudent"));
        firstTagList.add(new Tag("PotentialTA"));

        Id secondStudentId = new Id("A1029384A");
        HashSet<Tag> secondTagList = new HashSet<Tag>();
        secondTagList.add(new Tag("TopStudent"));
        secondTagList.add(new Tag("PotentialTA"));

        Id thirdStudentId = new Id("A2129334F");
        HashSet<Tag> thirdTagList = new HashSet<Tag>();
        thirdTagList.add(new Tag("BadStudent"));
        thirdTagList.add(new Tag("GoodStudent"));

        TagCommand firstTagCommand = new TagCommand(firstStudentId, firstTagList);
        TagCommand secondTagCommand = new TagCommand(secondStudentId, secondTagList);
        TagCommand thirdTagCommand = new TagCommand(thirdStudentId, thirdTagList);

        // same object -> returns true
        assertTrue(firstTagCommand.equals(firstTagCommand));

        // same values -> returns true
        assertTrue(firstTagCommand.equals(secondTagCommand));

        // different types -> returns false
        assertFalse(firstTagCommand.equals(1));

        // null -> returns false
        assertFalse(firstTagCommand.equals(null));

        // different person -> returns false
        assertFalse(firstTagCommand.equals(thirdTagCommand));
    }

    @Test
    public void execute_noStudentFound_throwCommandException() {
        HashSet<Tag> tagList = new HashSet<Tag>();
        tagList.add(new Tag("TopStudent"));
        Id invalidId = new Id("A0912124E");
        TagCommand tagCommand = new TagCommand(invalidId, tagList);

        assertCommandFailure(tagCommand, model, TagCommand.MESSAGE_PERSON_NOTFOUND);
    }

    @Test
    public void execute_totallyDuplicateTag_throwCommandException() {
        HashSet<Tag> tagList = new HashSet<Tag>();
        tagList.add(new Tag("friends"));
        Id validId = new Id("A0265901E");
        TagCommand tagCommand = new TagCommand(validId, tagList);
        String expectedErrorMessage = MESSAGE_DUPLICATE;

        assertCommandFailure(tagCommand, model, expectedErrorMessage);
    }

    @Test
    public void execute_partiallyDuplicateTag_throwCommandException() {
        HashSet<Tag> tagList = new HashSet<Tag>();
        tagList.add(new Tag("friends"));
        tagList.add(new Tag("topstudent"));
        Id validId = new Id("A0265901E");
        TagCommand tagCommand = new TagCommand(validId, tagList);
        String expectedErrorMessage = MESSAGE_DUPLICATE;

        assertCommandFailure(tagCommand, model, expectedErrorMessage);
    }

    @Test
    public void execute_validIdValidTag_success() {
        HashSet<Tag> tagList = new HashSet<Tag>();
        tagList.add(new Tag("TopStudent"));
        Id validId = new Id("A0251893P");
        TagCommand tagCommand = new TagCommand(validId, tagList);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Student resultStudent = new PersonBuilder().withName("Alice Pauline").withId("A0251893P")
            .withMajor("Computer Science").withIntake("2023")
            .withGrade(new Grade(new Course("CS2113"), new Score(55)))
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("94351253")
            .withTags("TA", "Smart", "friends", "TopStudent").build();
        expectedModel.setPerson(ALICE, resultStudent);
        assertCommandSuccess(tagCommand, model, String.format(MESSAGE_ADD_TAG_SUCCESS, tagList), expectedModel);
    }

    @Test
    public void toStringMethod() {
        HashSet<Tag> tagList = new HashSet<Tag>();
        tagList.add(new Tag("TopStudent"));
        Id invalidId = new Id("A0912124E");
        TagCommand tagCommand = new TagCommand(invalidId, tagList);

        String expected = TagCommand.class.getCanonicalName() + "{Id=" + "A0912124E, " + "Tags=" + tagList + "}";
        assertEquals(expected, tagCommand.toString());
    }
}
