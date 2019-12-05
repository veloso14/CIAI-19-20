import React, {useState} from 'react';
import {Vet} from "./VetList";
import {Appointment} from "./AppointmentList";
import {GlobalState} from "../App";
import {connect} from "react-redux";
import {useParams} from "react-router-dom"
import useForm from "react-hook-form";
import Container from "react-bootstrap/Container";
import Image from "react-bootstrap/Image"
import Card from "react-bootstrap/Card";
import Spinner from "react-bootstrap/Spinner";
import {Schedule} from "./VetList";
import {fetchVet, receiveVet, setScheduleVet, updateVetRequest} from "../actions/VetActions";
import {getData} from "../Utils/NetworkUtils";

type FormData = {
    month: string
}


const VetDetails = (props: { vet: Vet, isFetching: boolean, appointments: Appointment[], schedules: Schedule[], loadVet: (id: string) => void, updateVet: (id: string, vet: Vet) => void ,  postScheduleVet: (id: string, month: Schedule) => void}) => {
    let {id} = useParams();

    const [vet, setVet] = useState({} as Vet)

    const {register, setValue, handleSubmit, errors} = useForm<FormData>();

    const onSubmit = handleSubmit(({month}) => {
        console.log(month);
        props.postScheduleVet(id as string,{month: month})
        setValue("month", "JAN")
    });

    const fetchVet = (id:string) => {

        return getData(`/vets/${id}`, {} as Vet)
            .then(data => {

                data && setVet(data)
            })
    }

    React.useEffect(() => {
        console.log("run effect details: " + id as string);
        //props.loadVet(id as string);
        fetchVet(id as string)
    }, []);

    const content = (
        <Container>
            <br/>
            <h1 className="text-center">Details</h1><br/>

            <Image width={200} height={200} fluid src={require('../images/user.jpg')} roundedCircle/>

            <div>
                <h5>Vet name: </h5>
                <p>{vet.name}</p>
                <h5>Cellphone: </h5><p>{vet.cellphone}</p>
                <h5>Email: </h5><p>{vet.email}</p>
                <h5>Address: </h5><p>{vet.address}</p>
            </div>

            <br/>

            <br/>
                <Card>
                    <Card.Header>

                        <h1 className="text-center">Set schedule</h1>

                    </Card.Header>
                        <Card.Body>

                            <form onSubmit={onSubmit}>
                                <div className="form-group">
                                    <label>Month</label>
                                    <select className="form-control" id="month" name="month" ref={register({required: true})}>
                                        <option value="JAN">January</option>
                                        <option value="FEB">February</option>
                                        <option value="MAR">March</option>
                                        <option value="APR">April</option>
                                        <option value="MAY">May</option>
                                        <option value="JUN">June</option>
                                        <option value="JUL">July</option>
                                        <option value="AUG">August</option>
                                        <option value="SEP">September</option>
                                        <option value="OCT">October</option>
                                        <option value="NOV">November</option>
                                        <option value="DEC">December</option>
                                    </select>
                                </div>
                                <input className="btn btn-primary float-right" type="submit" value="Set schedule"/>
                            </form>
                        </Card.Body>

                </Card>


            <br/>
        </Container>

    );

    const loadingContent = (
        <Spinner animation="border" role="status">
            <span className="sr-only">Loading...</span>
        </Spinner>
    );

    return (
        <Container>
            {props.isFetching ? loadingContent : content}
        </Container>
    );

};


const mapStateToProps = (state: GlobalState) => {
    return {
        vet: state.vets.vet,
        appointments: state.vets.appointments,
        schedules: state.vets.schedules,
        isFetching: state.vets.isFetching
    }
};
const mapDispatchToProps = (dispatch: any) => {
    return {
        loadVet: (id: string) => {
            dispatch(fetchVet(id))
        },
        updateVet: (id: string, newVet: Vet) => {
            dispatch(updateVetRequest(id, newVet))
        },
        postScheduleVet: (id: string, month: Schedule)=> {
            dispatch(setScheduleVet(id, month))
        },
    }
};
export default connect(mapStateToProps, mapDispatchToProps)(VetDetails);