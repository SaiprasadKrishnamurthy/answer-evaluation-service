import React, { Fragment } from 'react'
import { ExampleWrapperSeamless, PageTitle } from 'layout-components'
import { Grid, Card, Divider, TextField, Button } from '@material-ui/core'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'



export default function Answers() {
    return (<Fragment>
        <PageTitle
            titleHeading="Questionaire"
            titleDescription="Please upload your answers here"
        />
        <form /* onSubmit={handleSubmit} */>
            <ExampleWrapperSeamless sectionHeading="Answer Sheet">
                <Grid container spacing={4}>
                    <Grid item xs={12} lg={12}>
                        <Card className="p-4 mb-4">
                            <div className="font-size-lg font-weight-bold">Section</div>
                            <Divider className="my-4" />
                            <Grid container spacing={4}>
                                <Grid item xs={12} lg={12}>
                                    <TextField
                                        fullWidth
                                        className="m-2"
                                        id="standard-multiline-static"
                                        label="Answer"
                                        multiline
                                        rows="4"
                                    />
                                </Grid>

                            </Grid>
                        </Card>
                    </Grid>
                </Grid>
            </ExampleWrapperSeamless>
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
    </Fragment>)
}