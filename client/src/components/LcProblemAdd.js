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
    const value = event.target.id === 'problemId' ? parseInt(event.target.value) : event.target.value;
    problem[event.target.id] = value;
    setProblem(problem)
  }

  const onSubmitHandler = (e) => {
    // Validate required fields
    if (!problem.problemId) {
      alert.error('Problem ID is required');
      return;
    }
    if (!problem.description) {
      alert.error('Description is required');
      return;
    }

    const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(problem)
    };

    console.log('Sending request with data:', problem);

    fetch('http://localhost:8080/api/lcProblem', requestOptions)
      .then(async (res) => {
        console.log('Response status:', res.status);
        const responseText = await res.text();
        console.log('Response body:', responseText);
        
        if (res.status !== 200) {
          alert.error(`Failed to add the record: ${responseText}`);
        } else {
          setRender(!toggle);
          alert.success('Added record successfully');
        }
      })
      .catch(error => {
        console.error('Error:', error);
        alert.error(`Failed to add the record: ${error.message}`);
      });
  }

  return (
    <StyledForm noValidate autoComplete="off">
      <TextField 
        id="problemId" 
        label="Problem Id" 
        onChange={setProblemObj}
        type="number"
        required
      />
      <TextField 
        id="description" 
        label="Problem Description" 
        onChange={setProblemObj}
        required
      />
      <TextField 
        id="comments" 
        label="Comment" 
        onChange={setProblemObj}
      />
      &nbsp;&nbsp;&nbsp;
      <Button variant="contained" color="primary" onClick={onSubmitHandler}>Add</Button>
    </StyledForm>
  );
}

export default LcProblemAdd;