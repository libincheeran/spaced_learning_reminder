import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import { Button } from "@material-ui/core";
import { useAlert } from 'react-alert'

const useStyles = makeStyles((theme) => ({
  root: {
    '& > *': {
      margin: theme.spacing(1),
      width: '25ch',
    },
  },
}));

const LcProblemAdd = ({ toggle, setRender }) => {
  const alert = useAlert()
  const classes = useStyles();
  const [problem, setProblem] = useState({});

  const setProblemObj = (event) => {
    problem[event.target.id] = event.target.value;
    setProblem(problem)
  }

  const onSubmitHandler = (e) => {

    const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(problem)
    };
    fetch('http://localhost:8080/api/lcProblem', requestOptions)
      .then(res => {
        console.log(res);
        if (res.status !== 200) {
          alert.error('Failed to add the record')
        } else {
          setRender(!toggle);
          alert.success('Added record successfully')
        }
      })

    console.log(" called onSubmit with " + JSON.stringify(problem))
  }

  return (
    <form className={classes.root} noValidate autoComplete="off">
      <TextField id="problemId" label="Problem Id" onChange={setProblemObj} />
      <TextField id="description" label="Problem Description" onChange={setProblemObj} />
      <TextField id="comments" label="Comment" onChange={setProblemObj} />
      &nbsp;&nbsp;&nbsp;
      <Button variant="contained" color="primary" onClick={onSubmitHandler}>Add</Button>
    </form>
  );
}

export default LcProblemAdd;