import React, { useState } from 'react';
import { Fragment } from 'react';
import { PageTitle, ExampleWrapperSeamless } from 'layout-components';
import { Divider, Grid, Card, TextField, IconButton, CardContent, Box, Button } from '@material-ui/core';
import { ifElse, isEmpty, complement, compose, trim, map, either, remove, uniq, reverse, isNil, lensProp, set, omit, gt, length, __ } from 'ramda';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { postQuestion, getTopics } from './services.js'
import useSignUpForm from '../../helpers/FormHooks.js';
import { useSnackbar } from 'notistack';
export default function AdminLanding() {

    const [annotations, setAnnotations] = useState(null);
    const [entries, setEntries] = useState([]);
    const [topics, setTopics] = useState([]);
    const { enqueueSnackbar } = useSnackbar();
    const initialFormState = {
        id: "1",
        author: "PJ",
        identifier: {
            clientId: 'National Institute Of Technology',
            classId: 'WED4321',
            subjectId: 'XSV1234',
            questionId: "1"
        },
        rawQuestion: "",
        rawAnswer: "",
        totalMarks: 0,
        maxMarks: 0
    }


    const handleSelect = () => {
        /* Beginning  of functions */
        const checkifGlobalSetorSelectedisEmpty = () => entries.length > 0;
        const hasContent = x => ifElse(either(complement(isEmpty), checkifGlobalSetorSelectedisEmpty), addingAnnotationBox, isNotSelected)(x)
        const isNotSelected = () => null;
        const listingPhrases = x => { setEntries([...entries, ...[trim(x)]]); return [...entries, ...[trim(x)]] }
        const removeDuplicates = x => { setEntries(uniq(x)); return uniq(x) }
        const annotationWithSpace = x => map(createObject, x);
        const createObject = x => Object.assign({}, { isPhrase: true, keyword: x, marks: 1, synonyms: [] })
        /* Uncomment the following if you want to detect keyword or phrase  
        const checkForSpace = x => /^\S+$/gm.test(x)
         const createObject = (x, y) => Object.assign({}, { isPhrase: x, keyword: y, marks: 1, synonyms: [] })
         const addPhrase = partial(createObject, [true])
         const addWord = partial(createObject, [false])
         const checkIfPhraseorWord = x => ifElse(complement(checkForSpace), addPhrase, addWord)(x) */
        const addingAnnotationBox = x => compose(reverse, annotationWithSpace, removeDuplicates, listingPhrases)(x)
        const setAnnot = () => setAnnotations(hasContent(selectedContent))
        const checkForSelection = ifElse(gt(__, 0), setAnnot, isNotSelected)
        /* Ending of functions */
        let selectedContent = window.getSelection().toString();
        checkForSelection(length(selectedContent))
        /* hasContent(selectedContent) */
    }
    const deleteFromAnnotation = id => {
        const removeFromList = (x) => remove(id, 1, x);
        setAnnotations(removeFromList(annotations));
        setEntries(removeFromList(entries));

    }
    const loadTopic = () => {
        getTopics(inputs.rawAnswer).then((x) => setTopics(x))
    }
    const deleteFromTopics = id => setTopics(topics.filter((_, xid) => xid !== id))
    const signup = () => {
        /* inputs.keyword = [...annotations]; */
        /*   const consoleMe = x => console.log(topics)
          const consoleErr = x => console.error(x) */
        const warnUser = () => alert("Please select phrases from expected answer which need to be graded")
        const xKeyWord = lensProp('keywords');
        const removePhrase = [...annotations].map(x => omit(['isPhrase'], x))
        const setAnnotationsWithKeywords = (inputs) => set(xKeyWord, removePhrase, { ...inputs });
        const xTopics = lensProp('topics')
        const setTopicsWithKeyTopic = (inputs) => set(xTopics, [...topics], { ...inputs })
        const composePostBodyWithInput = () => compose(setAnnotationsWithKeywords, /* setTopicsWithKeyTopic */)(inputs)
        const valueForPost = x => ifElse(isNil, warnUser, composePostBodyWithInput)(x)
        let output = valueForPost(annotations);
        console.log(output)
        postQuestion(output).then(() => enqueueSnackbar('Question successfully submitted', { variant: 'success' }))
            .catch(() => enqueueSnackbar('Question successfully submitted', { variant: 'success' }));
    }
    const { inputs, handleInputChange, handleNumericInputChange, handleSubmit } = useSignUpForm(signup, initialFormState);
    return (
        <Fragment>
            <PageTitle
                titleHeading="Questionaire Upload"
                titleDescription="Please upload your questions here"
            />
            <form onSubmit={handleSubmit}>
                <ExampleWrapperSeamless sectionHeading="Please fill all the details">
                    <Grid container spacing={4}>
                        <Grid item xs={12} lg={12}>
                            <Card className="p-4 mb-4">
                                <div className="font-size-lg font-weight-bold">University Info</div>
                                <Divider className="my-4" />
                                <Grid container spacing={4}>
                                    <Grid item xs={12} lg={6}>
                                        <TextField
                                            fullWidth
                                            className="m-2"
                                            id="standard-basic1"
                                            label="Client Id"
                                            name="clientId"
                                            onChange={handleInputChange}
                                            value={inputs.identifier.clientId}
                                        />
                                    </Grid>
                                    <Grid item xs={12} lg={6}>
                                        <TextField
                                            fullWidth
                                            className="m-2"
                                            id="standard-basic2"
                                            name="classId"
                                            label="Class Id"
                                            value={inputs.identifier.classId}
                                        />
                                    </Grid>
                                    <Grid item xs={12} lg={6}>
                                        <TextField
                                            fullWidth
                                            className="m-2"
                                            id="standard-basic3"
                                            name="subjectId"
                                            label="Subject Id"
                                            value={inputs.identifier.subjectId}
                                            onChange={handleInputChange}
                                        />
                                    </Grid>
                                </Grid>
                            </Card>
                        </Grid>
                    </Grid>
                </ExampleWrapperSeamless>
                <ExampleWrapperSeamless>
                    <Grid container spacing={4}>
                        <Grid item xs={12} lg={12}>
                            <Card className="p-4 mb-4">
                                <div className="font-size-lg font-weight-bold">Question Details</div>
                                <Divider className="my-4" />
                                <Grid container spacing={4}>
                                    <Grid item xs={12} lg={2}>
                                        <TextField
                                            fullWidth
                                            className="m-2"
                                            id="standard-basic3"
                                            name="identifier.questionId"
                                            label="Question Id"
                                            onChange={handleInputChange}
                                            value={inputs.identifier.questionId}
                                        />
                                    </Grid>
                                    <Grid item xs={12} lg={10}>
                                        <TextField
                                            fullWidth
                                            className="m-2"
                                            id="standard-basic3"
                                            name="rawQuestion"
                                            label="Question"
                                            value={inputs.rawQuestion}
                                            onChange={handleInputChange}
                                        />

                                    </Grid>
                                    <Grid item xs={12} lg={12}>
                                        <Grid container>

                                            <Grid item xs={12} lg={11}>
                                                <TextField
                                                    fullWidth
                                                    className="m-2"
                                                    id="standard-multiline-static"
                                                    label="Expected Answer"
                                                    name="rawAnswer"
                                                    multiline
                                                    rows="4"
                                                    onMouseUp={handleSelect}
                                                    value={inputs.rawAnswer}
                                                    onChange={handleInputChange}
                                                />
                                            </Grid>
                                            <Grid item xs={12} lg={1} style={{ "alignSelf": "flex-end" }}>
                                                <Button variant="contained" color="secondary" className="ml-3 p-2" onClick={loadTopic}>
                                                    <span className="btn-wrapper--icon">
                                                        <FontAwesomeIcon icon={"check"} />
                                                    </span>
                                                    <span className="btn-wrapper--label">Lookup</span>
                                                </Button>
                                            </Grid>
                                        </Grid>
                                    </Grid>
                                    <Grid item xs={12} lg={6}>
                                        <TextField
                                            fullWidth
                                            className="m-2"
                                            id="standard-basic4"
                                            label="Total Marks"
                                            name="totalMarks"
                                            value={inputs.totalMarks}
                                            onChange={handleNumericInputChange}
                                        />
                                    </Grid>
                                    <Grid item xs={12} lg={6}>
                                        <TextField
                                            fullWidth
                                            className="m-2"
                                            id="standard-basic5"
                                            label="Max Marks"
                                            name="maxMarks"
                                            value={inputs.maxMarks}
                                            onChange={handleNumericInputChange}
                                        />
                                    </Grid>
                                </Grid>
                            </Card>
                        </Grid>
                    </Grid>
                </ExampleWrapperSeamless>
                <Card className="card-box mb-4">
                    <CardContent className="p-0">
                        <div className="table-responsive">
                            <table className="table table-striped table-hover text-nowrap mb-0">
                                <thead className="thead-light">
                                    <tr>
                                        <th style={{ width: '40%' }}>Exact Keywords</th>
                                        <th className="text-center">Keyword/Exact</th>
                                        <th className="text-center">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <Fragment>
                                        {annotations !== null && annotations.map((x, id) => <tr key={id}>
                                            <td>
                                                <div className="d-flex align-items-center">
                                                    <div>
                                                        <a
                                                            href="#/"
                                                            onClick={e => e.preventDefault()}
                                                            className="font-weight-bold text-black"
                                                            title="...">{x.keyword}</a>
                                                    </div>
                                                </div>
                                            </td>
                                            <td className="text-center"> <div className="h-auto py-0 px-3 ">
                                                <div>
                                                    <span
                                                        onClick={e => e.preventDefault()}
                                                        className="font-weight-bold text-black"
                                                        title="...">{x.isPhrase ? "Phrase" : "Keyword"}</span>
                                                </div>
                                            </div></td>
                                            <td className="text-center">
                                                <Box >
                                                    <IconButton
                                                        size="small"
                                                        color="primary"
                                                        className="text-danger"
                                                        title="View details"
                                                        onClick={() => deleteFromAnnotation(id)}
                                                    >
                                                        <FontAwesomeIcon
                                                            icon="trash"
                                                            className="font-size-lg"
                                                        />
                                                    </IconButton>
                                                </Box>
                                            </td>

                                        </tr>
                                        )}</Fragment>
                                </tbody>
                            </table>
                        </div>
                    </CardContent>
                </Card>
                {topics.length > 0 && <Card className="card-box mb-4">
                    <CardContent className="p-0">
                        <div className="table-responsive">
                            <table className="table table-striped table-hover text-nowrap mb-0">
                                <thead className="thead-light">
                                    <tr>
                                        <th style={{ width: '40%' }}>Value</th>
                                        <th className="text-center">Topic</th>
                                        <th className="text-center">Delete</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <Fragment>
                                        {topics !== null && topics.map((x, id) => <tr key={id}>
                                            <td>
                                                <div className="d-flex align-items-center">
                                                    <div>
                                                        <a
                                                            href="#/"
                                                            onClick={e => e.preventDefault()}
                                                            className="font-weight-bold text-black"
                                                            title="...">{x}</a>
                                                    </div>
                                                </div>
                                            </td>
                                            <td className="text-center"> <div className="h-auto py-0 px-3 ">
                                                <div>
                                                    <span
                                                        onClick={e => e.preventDefault()}
                                                        className="font-weight-bold text-black"
                                                        title="...">{x}</span>
                                                </div>
                                            </div></td>
                                            <td className="text-center">
                                                <Box >
                                                    <IconButton
                                                        size="small"
                                                        color="primary"
                                                        className="text-danger"
                                                        title="View details"
                                                        onClick={() => deleteFromTopics(id)}
                                                    >
                                                        <FontAwesomeIcon
                                                            icon="trash"
                                                            className="font-size-lg"
                                                        />
                                                    </IconButton>
                                                </Box>
                                            </td>

                                        </tr>
                                        )}</Fragment>
                                </tbody>
                            </table>
                        </div>
                    </CardContent>
                </Card>}
                <ExampleWrapperSeamless>
                    <Grid container spacing={4}>
                        <Grid item xs={12} lg={6} style={{ display: "flex", justifyContent: "flex-end" }}>
                            <Button variant="contained" color="primary" className="m-2" type="submit">
                                <span className="btn-wrapper--icon">
                                    <FontAwesomeIcon icon={"check"} />
                                </span>
                                <span className="btn-wrapper--label">Submit</span>
                            </Button>
                        </Grid>
                        <Grid item xs={12} lg={6}>
                            <Button variant="contained" color="default" className="m-2" >
                                <span className="btn-wrapper--icon">
                                    <FontAwesomeIcon icon={"undo"} />
                                </span>
                                <span className="btn-wrapper--label">Reset</span>
                            </Button>
                        </Grid>
                    </Grid>
                </ExampleWrapperSeamless>
            </form>
        </Fragment>
    );
}
