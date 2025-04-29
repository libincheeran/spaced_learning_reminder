import React, { useState } from 'react';
import { styled } from '@mui/material/styles';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import { useAlert } from 'react-alert'

const StyledForm = styled('form')(({ theme }) => ({
  '& > *': {
    margin: theme.spacing(1),
    width: '25ch',
  },
}));

const LcProblemAdd = ({ toggle, setRender }) => {
  const alert = useAlert()
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
    <StyledForm noValidate autoComplete="off">
      <TextField id="problemId" label="Problem Id" onChange={setProblemObj} />
      <TextField id="description" label="Problem Description" onChange={setProblemObj} />
      <TextField id="comments" label="Comment" onChange={setProblemObj} />
      &nbsp;&nbsp;&nbsp;
      <Button variant="contained" color="primary" onClick={onSubmitHandler}>Add</Button>
    </StyledForm>
  );
}

export default LcProblemAdd;